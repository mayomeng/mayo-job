package mayo.job.store.impl;

import mayo.job.parent.param.JobParam;
import mayo.job.store.AsyncJobStorer;
import mayo.job.store.JobKeyCreator;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 任务存储
 */
@Component
public class RedisJobStorer implements AsyncJobStorer {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private Mapper mapper;

    // 分配任务脚本
    private DefaultRedisScript allotScript;
    // 取得多个value脚本
    private DefaultRedisScript getValuesScript;

    @PostConstruct
    public void init() {
        allotScript = new DefaultRedisScript();
        allotScript.setLocation(new ClassPathResource("lua/allotJob.lua"));

        getValuesScript = new DefaultRedisScript();
        getValuesScript.setLocation(new ClassPathResource("lua/getValues.lua"));
        getValuesScript.setResultType(String.class);
    }

    /**
     * 创建任务
     */
    @Override
    public long createJob(JobParam jobParam) {
        long jobId = redisTemplate.opsForValue().increment(JobKeyCreator.getJobIdKey(), 1L);
        jobParam.setJobId(jobId);
        redisTemplate.opsForList().rightPush(JobKeyCreator.getUnallotJobListKey(jobParam.getJobName()), jobParam);
        return jobId;
    }

    /**
     * 重新添加任务
     */
    @Override
    public long reCreateJob(JobParam jobParam) {
        redisTemplate.opsForList().rightPush(JobKeyCreator.getUnallotJobListKey(jobParam.getJobName()), jobParam);
        return jobParam.getJobId();
    }

    /**
     * 取得任务执行结果
     */
    @Override
    public JobParam getJobResult(long jobId) {
        Map jobResultMap =  redisTemplate.opsForHash().entries(JobKeyCreator.getJobKey(jobId));
        return mapper.map(jobResultMap, JobParam.class);
    }

    @Override
    public boolean isJobComplete(long jobId) {
        return redisTemplate.hasKey(JobKeyCreator.getJobKey(jobId));
    }

    /**
     * 设置任务执行结果
     */
    @Override
    public void setJobResult(JobParam jobResult) {
        redisTemplate.opsForHash().putAll(
                JobKeyCreator.getJobKey(jobResult.getJobId()), mapper.map(jobResult, HashMap.class));
    }

    /**
     * 持久化任务执行数据
     */
    @Override
    public long persistenceJobResume(JobParam jobResult) {
        return 0;
    }

    /**
     * 分配任务
     */
    @Override
    public void allotJob(String nodeId, String jobName, int count) {
        redisTemplate.execute(allotScript,
                Arrays.asList(JobKeyCreator.getUnallotJobListKey(jobName), JobKeyCreator.getPendingJobKey(nodeId, jobName)),
                count);
    }

    /**
     * 取得待执行任务个数
     */
    @Override
    public long getPendingJobCount(String nodeId, String jobName) {
        return redisTemplate.opsForList().size(JobKeyCreator.getPendingJobKey(nodeId, jobName));
    }

    /**
     * 拉取任务至执行器
     */
    @Override
    public List<Object> pullMultipleJob(String nodeId, String jobName, int count) {
        List<Object> list = new ArrayList<>();
        for (int i = 0 ; i < count ; i++) {
            Object jobParam = redisTemplate.opsForList().rightPop(JobKeyCreator.getPendingJobKey(nodeId, jobName));
            if (jobParam != null) {
                list.add(jobParam);
            }
        }
        return list;
    }

    /**
     * 删除执行完的任务
     */
    @Override
    public void removeJob(long jobId) {
        redisTemplate.delete(JobKeyCreator.getJobKey(jobId));
    }

    /**
     * 将该节点上的任务重新分配给其他节点
     */
    @Override
    public void reAllotJob(String nodeId, String jobName) {
        long count = redisTemplate.opsForList().size(JobKeyCreator.getPendingJobKey(nodeId, jobName));
        redisTemplate.execute(allotScript,
                Arrays.asList(JobKeyCreator.getPendingJobKey(nodeId, jobName),
                        JobKeyCreator.getUnallotJobListKey(jobName)),
                count);
    }

    /**
     * 将执行完的任务状态置为完成
     */
    @Override
    public void completeJob(String jobName) {
        // TODO
    }

    /**
     * 取得多个key的value(可以用mget实现)
     */
    public List<Object> getObjectListSp(List<String> keyList) {
        List<Object> resultList = new ArrayList<>();
        resultList = redisTemplate.executePipelined(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (String key : keyList) {
                    connection.get(redisTemplate.getStringSerializer().serialize(key));
                }
                return null;
            }
        });
        return resultList;
    }

    /**
     * 取得多个key的value(如果key不存在则value返回null)
     */
    public Map<String, Object> getObjectMapSp(List<String> keyList) {
        List<Object> resultList = (List<Object>)redisTemplate.execute(getValuesScript,
                keyList,
                new ArrayList<>());
        Map<String, Object> resultMap = new HashMap<>();
        for (int i = 0 ; i < keyList.size() ; i++) {
            if (resultList.get(i) != null) {
                resultMap.put(keyList.get(i), resultList.get(i));
            }
        }
        return resultMap;
    }
}

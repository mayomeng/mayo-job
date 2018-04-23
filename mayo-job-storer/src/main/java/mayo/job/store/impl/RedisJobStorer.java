package mayo.job.store.impl;

import mayo.job.parent.param.JobParam;
import mayo.job.store.AsyncJobStorer;
import mayo.job.store.JobKeyCreator;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务存储
 */
@Component
public class RedisJobStorer implements AsyncJobStorer {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private Mapper mapper;

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
        for (int i = 0 ; i < count ; i++) {
            redisTemplate.opsForList().rightPopAndLeftPush(
                    JobKeyCreator.getUnallotJobListKey(jobName),
                    JobKeyCreator.getPendingJobKey(nodeId, jobName)/*,
                    1,
                    TimeUnit.SECONDS*/
            );
        }
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
            Object jopParam = redisTemplate.opsForList().rightPop(JobKeyCreator.getPendingJobKey(nodeId, jobName));
            if (jopParam != null) {
                list.add(jopParam);
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

    }

    /**
     * 将执行完的任务状态置为完成
     */
    @Override
    public void completeJob(String jobName) {
        // TODO
    }
}

package mayo.job.store.redis;

import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;
import mayo.job.store.AsyncJobStorer;
import mayo.job.store.JobKeyCreator;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
     * 取得任务执行结果
     */
    @Override
    public JobResult getJobResult(long jobId) {
        Map jobResultMap =  redisTemplate.opsForHash().entries(JobKeyCreator.getJobKey(jobId));
        return mapper.map(jobResultMap, JobResult.class);
    }

    /**
     * 设置任务执行结果
     */
    @Override
    public void setJobResult(JobResult jobResult) {
        redisTemplate.opsForHash().putAll(
                JobKeyCreator.getJobKey(jobResult.getJobId()), mapper.map(jobResult, HashMap.class));
    }

    /**
     * 持久化任务执行数据
     */
    @Override
    public long persistenceJobResume(JobResult jobResult) {
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
                    JobKeyCreator.getPendingJobKey(nodeId, jobName)
            );
        }
    }

    /**
     * 将待处理任务分配给任务执行节点
     */
    @Override
    public void prepareJob(String nodeId, String jobName, int count) {
        for (int i = 0 ; i < count ; i++) {
            Object o = redisTemplate.opsForList().rightPopAndLeftPush(
                    JobKeyCreator.getPendingJobKey(nodeId, jobName),
                    JobKeyCreator.getHandlingJobList(nodeId, jobName),
                    1,
                    TimeUnit.SECONDS
            );
            System.out.println(o);
        }
    }

    /**
     * 拉取任务至执行器
     */
    @Override
    public List<Object> pullMultipleJob(String nodeId, String jobName) {
        return redisTemplate.opsForList().range(JobKeyCreator.getHandlingJobList(nodeId, jobName), 0, -1);
    }

    /**
     * 删除执行完的任务
     */
    @Override
    public void clearHandlingJobList(String nodeId, String jobName) {
        redisTemplate.opsForList().remove(JobKeyCreator.getHandlingJobList(nodeId, jobName), -1, null);
    }

    /**
     * 将执行完的任务状态置为完成
     */
    @Override
    public void completeJob(String jobName) {

    }
}

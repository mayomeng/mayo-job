package mayo.job.store;

import java.util.List;

/**
 * 异步任务存储
 */
public interface AsyncJobStorer extends  JobStorer {
    /**
     * 分配任务
     */
    void allotJob(String nodeId, String jobName, int count);
    /**
     * 取得待执行任务个数
     */
    long getPendingJobCount(String nodeId, String jobName);
    /**
     * 拉取任务至执行器
     */
    List<Object> pullMultipleJob(String nodeId, String jobName, int count);
    /**
     * 删除执行完的任务
     */
    void removeJob(long jobId);

    void reAllotJob();
    /**
     * 将执行完的任务状态置为完成
     */
    void completeJob(String jobName);
}

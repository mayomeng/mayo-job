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
     * 使任务进入准备状态
     */
    void prepareJob(String nodeId, String jobName, int count);
    /**
     * 拉取任务至执行器
     */
    List pullMultipleJob(String nodeId, String jobName);
    /**
     * 删除执行完的任务
     */
    void clearHandlingJobList(String nodeId, String jobName);
    /**
     * 将执行完的任务状态置为完成
     */
    void completeJob(String jobName);
}

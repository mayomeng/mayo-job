package mayo.job.store;

import mayo.job.parent.param.JobParam;

/**
 * 任务存储器
 */
public interface JobStorer {
    /**
     * 创建任务
     */
    long createJob(JobParam jobParam);
    /**
     * 重新添加任务
     */
    long reCreateJob(JobParam jobParam);
    /**
     * 取得任务执行结果
     */
    JobParam getJobResult(long jobId);
    /**
     * 取得任务是否执行完毕
     */
    boolean isJobComplete(long jobId);
    /**
     * 设置任务执行结果
     */
    void setJobResult(JobParam jobResult);
    /**
     * 持久化任务执行记录
     */
    long persistenceJobResume(JobParam jobResult);
}

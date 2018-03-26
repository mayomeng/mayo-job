package mayo.job.store;

import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;

/**
 * 任务持久层
 */
public interface JobStorer {
    /**
     * 创建任务
     */
    long createJob(JobParam jobParam);
    /**
     * 取得任务执行结果
     */
    JobResult getJobResult(long jobId);
}

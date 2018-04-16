package mayo.job.client;

import mayo.job.parent.param.JobParam;

/**
 * 任务客户端.
 */
public interface JobClient {
    /**
     * 同步请求，即时返回执行结果.
     */
    JobParam syncRequest(JobParam jobParam);

    /**
     * 异步请求,返回任务ID
     */
    long asynRequest(JobParam jobParam);

    /**
     * 查询任务执行结果
     */
    JobParam queryResult(long jobId);
}

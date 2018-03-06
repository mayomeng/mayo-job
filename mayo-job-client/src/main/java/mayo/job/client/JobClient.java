package mayo.job.client;

import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

/**
 * 任务客户端.
 */
public interface JobClient {
    /**
     * 同步请求，即时返回执行结果.
     */
    JobResult syncRequest(JobParam jobParam);

    /**
     * 异步请求,返回任务ID
     */
    long asynRequest(JobParam jobParam);

    /**
     * 查询任务执行结果
     */
    JobResult queryResult(long jobId);
}

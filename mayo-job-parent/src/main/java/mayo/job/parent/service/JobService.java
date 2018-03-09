package mayo.job.parent.service;

import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;

/**
 * 任务服务
 */
public interface JobService {
    /**
     * 初始化.
     */
    void init();
    /**
     * 返回服务任务类型.
     */
    String getJobType();
    /**
     * 执行任务.
     */
    Object execute(Object param);
}

package mayo.job.bean.job;

import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

/**
 * 普通任务.
 */
public interface Job {
    JobResult handle(JobParam jobParam);
    String getJobType();
    String getJobName();
}

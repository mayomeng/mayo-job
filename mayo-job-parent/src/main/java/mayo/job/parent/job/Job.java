package mayo.job.parent.job;

import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;

/**
 * 普通任务.
 */
public interface Job {
    JobResult handle(JobParam jobParam);
    String getJobType();
    String getJobName();
}

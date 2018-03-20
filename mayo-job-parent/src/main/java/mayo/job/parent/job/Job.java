package mayo.job.parent.job;

import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;

import java.util.Set;

/**
 * 普通任务.
 */
public interface Job {
    /**
     * 执行任务.
     */
    JobResult handle(JobParam jobParam);

    /**
     * 返回任务类型.
     */
    Set<String> getJobType();

    /**
     * 返回任务名称.
     */
    String getJobName();
}

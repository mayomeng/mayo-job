package mayo.job.parent.job;

import mayo.job.parent.param.JobParam;

import java.util.Set;

/**
 * 普通任务.
 */
public interface Job {
    /**
     * 执行任务.
     */
    JobParam handle(JobParam jobParam);

    /**
     * 返回任务类型.
     */
    Set<String> getJobType();
}

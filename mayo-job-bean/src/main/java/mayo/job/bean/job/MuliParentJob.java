package mayo.job.bean.job;

import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

/**
 * 并发任务(父任务)
 */
public class MuliParentJob implements Job {
    protected int jobCount; // 任务个数

    @Override
    public JobResult handle(JobParam jobParam) {
        return null;
    }

    @Override
    public String getJobType() {
        return null;
    }
}

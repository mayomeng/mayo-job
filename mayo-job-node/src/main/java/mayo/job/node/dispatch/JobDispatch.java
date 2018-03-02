package mayo.job.node.dispatch;

import mayo.job.bean.job.Job;
import mayo.job.bean.param.JobParam;
import mayo.job.node.JobNode;

/**
 * 任务调度器.
 */
public abstract class JobDispatch implements JobNode {

    /**
     * 分配任务.
     */
    @Override
    public Job execute(Object param) {
        JobParam jobParam = (JobParam)param;
        return executeJob(jobParam);
    }

    public abstract Job executeJob(JobParam jobParam);
}

package mayo.job.node.executer;

import mayo.job.bean.job.Job;
import mayo.job.bean.param.JobParam;
import mayo.job.node.JobNode;

import java.util.Map;

/**
 * 任务执行器
 */
public abstract class JobExecuter implements JobNode {

    /**
     * 执行任务.
     */
    @Override
    public Job execute(Object param) {
        JobParam jobParam = (JobParam)param;
        return executeJob(jobParam);
    }

    public abstract Job executeJob(JobParam jobParam);
}

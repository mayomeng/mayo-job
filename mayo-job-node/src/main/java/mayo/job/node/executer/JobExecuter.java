package mayo.job.node.executer;

import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;
import mayo.job.node.JobNode;;

/**
 * 任务执行器
 */
public interface JobExecuter extends JobNode {

    /**
     * 执行任务.
     */
    JobResult execute(JobParam param);
}

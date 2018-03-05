package mayo.job.node.dispatch;

import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;
import mayo.job.node.JobNode;;

/**
 * 任务调度器.
 */
public interface JobDispatch extends JobNode {

    /**
     * 分配任务.
     */
    JobResult dispatch(JobParam param);
}

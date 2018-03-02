package mayo.job.node.dispatch;

import mayo.job.bean.job.Job;
import mayo.job.bean.param.JobParam;
import mayo.job.node.JobNode;
import org.springframework.stereotype.Component;

/**
 * 任务调度器.
 */
@Component
public class JobDispatch implements JobNode {

    /**
     * 分配任务.
     */
    @Override
    public Job execute(Object param) {
        JobParam jobParam = (JobParam)param;
        return null;
    }
}

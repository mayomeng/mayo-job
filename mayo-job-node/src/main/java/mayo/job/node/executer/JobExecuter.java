package mayo.job.node.executer;

import mayo.job.bean.job.Job;
import mayo.job.bean.param.JobParam;
import mayo.job.node.JobNode;
import org.springframework.stereotype.Component;

/**
 * 任务执行器
 */
@Component
public class JobExecuter implements JobNode {
    /**
     * 执行任务.
     */
    @Override
    public Job execute(Object param) {
        JobParam jobParam = (JobParam)param;
        return null;
    }
}

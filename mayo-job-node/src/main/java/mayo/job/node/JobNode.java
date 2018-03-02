package mayo.job.node;

import mayo.job.bean.job.Job;
import mayo.job.bean.param.JobParam;

/**
 * 任务节点接口
 */
public interface JobNode {
    Job execute(Object param);
}

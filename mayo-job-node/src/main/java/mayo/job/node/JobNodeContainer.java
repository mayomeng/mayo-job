package mayo.job.node;

import mayo.job.bean.result.JobResult;
import mayo.job.bean.enums.NodeRoleEnum;
import mayo.job.bean.param.JobParam;
import mayo.job.node.coordinate.NodeRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 节点容器.
 */
@Component
public class JobNodeContainer {

    @Autowired
    private NodeRole nodeRole;

    @Autowired
    private JobDict jobDict;

    /**
     * 执行任务.
     */
    public JobResult execute(Object param) {
        JobParam jobParam = (JobParam)param;
        String jobType = jobDict.getJobType(jobParam.getJobName());
        if (NodeRoleEnum.ROLE_DISPATH.VALUE.equals(nodeRole.getRole())) {
            // 调度器的场合，对任务进行分配
            return jobDict.getJobDispatchMap().get(jobType).dispatch(jobParam);
        } else {
            // 执行器的场合执行任务
            return jobDict.getJobExecuterMap().get(jobType).execute(jobParam);
        }
    }
}

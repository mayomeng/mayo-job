package mayo.job.node;

import mayo.job.bean.enumBean.JobRoleEnum;
import mayo.job.node.coordinate.JobRole;
import mayo.job.node.dispatch.JobDispatch;
import mayo.job.node.executer.JobExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 节点容器.
 */
@Component
public class JobNodeContainer {

    @Autowired
    private JobDispatch jobDispatch;
    @Autowired
    private JobExecuter jobExecuter;
    @Autowired
    private JobRole jobRole;

    private Map<String, JobNode> jobNodeMap;

    @PostConstruct
    protected void init() {
        jobNodeMap = new HashMap<>();
        jobNodeMap.put(JobRoleEnum.ROLE_DISPATH.VALUE, jobDispatch);
        jobNodeMap.put(JobRoleEnum.ROLE_EXECUTER.VALUE, jobExecuter);
    }

    /**
     * 取得当前任务节点.
     */
    public JobNode getJobNode() {
        return jobNodeMap.get(jobRole.getRole());
    }
}

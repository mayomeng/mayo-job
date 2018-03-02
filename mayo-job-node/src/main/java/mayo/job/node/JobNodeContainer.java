package mayo.job.node;

import mayo.job.bean.enumBean.JobRoleEnum;
import mayo.job.bean.job.Job;
import mayo.job.bean.param.JobParam;
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
public class JobNodeContainer implements JobNode {

    private Map<String, JobDispatch> jobDispatchMap;
    private Map<String, JobExecuter> jobExecuterMap;
    @Autowired
    private JobRole jobRole;

    @PostConstruct
    protected void init() {
        // TODO 通过SPI加载所有的调度器和执行器
    }

    @Override
    public Job execute(Object param) {
        JobParam jobParam = (JobParam)param;
        if (JobRoleEnum.ROLE_DISPATH.VALUE.equals(jobRole.getRole())) {
            return jobDispatchMap.get(jobParam.getJobName()).execute(param);
        } else {
            return jobExecuterMap.get(jobParam.getJobName()).execute(param);
        }
    }
}

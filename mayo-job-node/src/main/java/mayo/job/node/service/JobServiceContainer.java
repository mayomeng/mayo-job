package mayo.job.node.service;

import mayo.job.parent.enums.NodeRoleEnum;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 节点容器.
 */
@Component("JobServiceContainer")
public class JobServiceContainer implements JobService {

    @Autowired
    private JobDict jobDict;

    @Autowired
    private JobEnvironment jobEnvironment;

    /**
     * 执行任务.
     */
    @Override
    public Object execute(Object param) {
        JobParam jobParam = (JobParam)param;
        String jobType = jobDict.getJobType(jobParam.getJobName());
        if (NodeRoleEnum.ROLE_DISPATH.VALUE.equals(jobEnvironment.getNodeRole())) {
            return jobDict.getJobDispatchMap().get(jobType).execute(jobParam);
        }
        return jobDict.getJobExecuterMap().get(jobType).execute(jobParam);
    }

    @Override
    @PostConstruct
    public void init() {
        jobEnvironment.setJobList(jobDict.getJobNameList());
    }

    @Override
    public String getJobType() {
        return null;
    }

    @Override
    public List<String> getJobNameList() {
        return jobDict.getJobNameList();
    }
}

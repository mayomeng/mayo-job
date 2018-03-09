package mayo.job.node;

import mayo.job.node.coordinate.JobCoordinate;
import mayo.job.parent.enums.NodeRoleEnum;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 节点容器.
 */
@Component("JobServiceContainer")
public class JobServiceContainer implements JobService {

    @Autowired
    private JobCoordinate jobCoordinate;

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
        if (NodeRoleEnum.ROLE_DISPATH.VALUE.equals(jobCoordinate.getRole())) {
            // 调度器的场合，对任务进行分配
            return jobDict.getJobDispatchMap().get(jobType).execute(jobParam);
        } else {
            // 执行器的场合执行任务
            return jobDict.getJobExecuterMap().get(jobType).execute(jobParam);
        }
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
}

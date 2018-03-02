package mayo.job.node.coordinate;

import mayo.job.bean.enumBean.JobRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务协调器，负责调度器选举，监控.
 */
@Component
public class JobCoordinate {

    @Autowired
    private JobRole jobRole;

    /**
     * 进行调度器选举
     */
    public void election() {

        if (true) {// TODO 选举成功
            jobRole.setRole(JobRoleEnum.ROLE_DISPATH.VALUE);
        } else {
            jobRole.setRole(JobRoleEnum.ROLE_EXECUTER.VALUE);
        }
    }

    /**
     * 监控
     */
    public void monitor() {
        if (JobRoleEnum.ROLE_DISPATH.VALUE.equals(jobRole.getRole())) {
            monitorExecuter();
        } else if (JobRoleEnum.ROLE_EXECUTER.VALUE.equals(jobRole.getRole())) {
            monitorDispath();
        }
    }

    /**
     * 监控调度器
     */
    private void monitorDispath() {
    }

    /**
     * 监控执行器
     */
    private void monitorExecuter() {
    }
}

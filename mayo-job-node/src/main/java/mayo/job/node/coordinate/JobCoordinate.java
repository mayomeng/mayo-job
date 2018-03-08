package mayo.job.node.coordinate;

import mayo.job.bean.enums.NodeRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务协调器，负责调度器选举，监控.
 */
@Component
public class JobCoordinate {

    @Autowired
    private JobNode jobNode;

    /**
     * 进行调度器选举
     */
    public void election() {
        // 选举前先将节点角色设为中间角色
        jobNode.setRole(NodeRoleEnum.ROLE_BETWEENNESS.VALUE);

        if (false) {// TODO 选举成功
            jobNode.setRole(NodeRoleEnum.ROLE_DISPATH.VALUE);
        } else {
            jobNode.setRole(NodeRoleEnum.ROLE_EXECUTER.VALUE);
        }
    }

    /**
     * 监控
     */
    public void monitor() {
        if (NodeRoleEnum.ROLE_DISPATH.VALUE.equals(jobNode.getRole())) {
            monitorExecuter();
        } else if (NodeRoleEnum.ROLE_EXECUTER.VALUE.equals(jobNode.getRole())) {
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

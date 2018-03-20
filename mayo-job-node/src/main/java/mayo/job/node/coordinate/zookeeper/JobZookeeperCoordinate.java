package mayo.job.node.coordinate.zookeeper;

import lombok.Getter;
import lombok.Setter;
import mayo.job.node.coordinate.JobCoordinate;
import mayo.job.parent.enums.NodeRoleEnum;
import org.springframework.stereotype.Component;

/**
 * 任务协调器，负责调度器选举，监控.(zookeeper实现)
 */
@Component
public class JobZookeeperCoordinate implements JobCoordinate {

    @Setter
    @Getter
    private String role;

    /**
     * 初始化
     */
    @Override
    public void init() {

    }

    /**
     * 进行调度器选举
     */
    @Override
    public void election() {
        // 选举前先将节点角色设为中间角色
        setRole(NodeRoleEnum.ROLE_BETWEENNESS.VALUE);

        if (false) {// TODO 选举成功
            setRole(NodeRoleEnum.ROLE_DISPATH.VALUE);
        } else {
            setRole(NodeRoleEnum.ROLE_EXECUTER.VALUE);
        }
    }

    /**
     * 监控
     */
    @Override
    public void monitor() {
        if (NodeRoleEnum.ROLE_DISPATH.VALUE.equals(getRole())) {
            monitorExecuter();
        } else if (NodeRoleEnum.ROLE_EXECUTER.VALUE.equals(getRole())) {
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

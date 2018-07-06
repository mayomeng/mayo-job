package mayo.job.node.impl;

import lombok.extern.slf4j.Slf4j;
import mayo.job.node.JobNode;
import mayo.job.node.coordinate.JobCoordinate;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.server.JobServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 本节点角色
 */
@Component
@Slf4j
public class JobNodeImpl implements JobNode {

    @Autowired
    private JobCoordinate jobCoordinate;
    @Autowired
    private JobServer jobServer;
    @Autowired
    private JobEnvironment jobEnvironment;

    @Override
    public void startup()  {
        try {
            // 启动选举监控
            jobCoordinate.election();

            // 设置环境变量
            jobEnvironment.setNodeRole(jobCoordinate.getRole());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void shutdown() {
        jobServer.shutdown();
        jobCoordinate.shutdown();
    }
}

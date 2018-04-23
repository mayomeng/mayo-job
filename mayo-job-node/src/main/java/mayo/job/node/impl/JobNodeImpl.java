package mayo.job.node.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.node.JobNode;
import mayo.job.node.coordinate.JobCoordinate;
import mayo.job.parent.enums.NodeTypeEnum;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.server.JobServer;
import mayo.job.parent.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
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
    @Qualifier("JobServiceContainer")
    private JobService jobService;
    @Autowired
    @Qualifier("syncServer")
    private JobServer syncServer;
    @Autowired
    @Qualifier("asyncServer")
    private JobServer asyncServer;
    @Autowired
    private JobEnvironment jobEnvironment;

    @Override
    public void startup()  {
        try {
            // 启动选举监控
            jobCoordinate.election();
            jobCoordinate.monitor();

            // 设置环境变量
            jobEnvironment.setNodeRole(jobCoordinate.getRole());

            if (NodeTypeEnum.TYPE_SYNC.VALUE.equals(jobEnvironment.getNodeType())) { // 启动同步服务器
                startupServer(syncServer);
            } else if (NodeTypeEnum.TYPE_ASYNC.VALUE.equals(jobEnvironment.getNodeType())) { // 启动异步服务器
                startupServer(asyncServer);
            } else { // 同时启动同步，异步服务器
                startupServer(syncServer);
                startupServer(asyncServer);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void shutdown() {
        if (NodeTypeEnum.TYPE_SYNC.VALUE.equals(jobEnvironment.getNodeType())) { // 启动同步服务器
            syncServer.shutdown();
        } else if (NodeTypeEnum.TYPE_ASYNC.VALUE.equals(jobEnvironment.getNodeType())) { // 启动异步服务器
            asyncServer.shutdown();
        } else { // 同时启动同步，异步服务器
            syncServer.shutdown();
            asyncServer.shutdown();
        }
    }

    /**
     * 启动服务器
     */
    private void startupServer(JobServer jobServer) {
        new Thread(() -> {
            jobServer.setService(jobService);
            try {
                jobServer.startup();
            } catch (Exception e) {
                jobServer.shutdown();
                log.error(e.getMessage(), e);
            }
        }).start();
    }
}

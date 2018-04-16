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
@PropertySource("classpath:job.properties")
@ConfigurationProperties(prefix = "node")
@Component
@Slf4j
public class JobNodeImpl implements JobNode {

    @Setter
    private String nodeId; // 节点ID
    @Setter
    private String nodeType; // 节点类型

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
    public void startup() {
        // 启动选举监控
        jobCoordinate.election();
        jobCoordinate.monitor();

        // 设置环境变量
        jobEnvironment.setNodeId(nodeId);
        jobEnvironment.setNodeRole(jobCoordinate.getRole());
        jobEnvironment.setNodeType(nodeType);

        if (NodeTypeEnum.TYPE_SYNC.VALUE.equals(nodeType)) { // 启动同步服务器
            startupSyncServer();
        } else if (NodeTypeEnum.TYPE_ASYNC.VALUE.equals(nodeType)) { // 启动异步服务器
            startupAsyncServer();
        } else { // 同时启动同步，异步服务器
            startupSyncServer();
            startupAsyncServer();
        }
    }

    /**
     * 启动同步服务器
     */
    private void startupSyncServer() {
        new Thread(() -> {
            syncServer.setService(jobService);
            try {
                syncServer.startup();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }).start();
    }

    /**
     * 启动异步服务器
     */
    private void startupAsyncServer() {
        new Thread(() -> {
            asyncServer.setService(jobService);
            try {
                asyncServer.startup();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }).start();
    }
}

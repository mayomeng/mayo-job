package mayo.job.node.impl;

import lombok.Setter;
import mayo.job.node.JobNode;
import mayo.job.node.coordinate.JobCoordinate;
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
public class JobNodeImpl implements JobNode {

    @Setter
    private String nodeId; // 节点ID

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
        // 设置环境变量
        jobEnvironment.setNodeId(nodeId);
        jobEnvironment.setRole(jobCoordinate.getRole());

        // 启动选举监控
        jobCoordinate.election();
        jobCoordinate.monitor();

        // 启动同步服务器
        syncServer.setService(jobService);
        syncServer.startup();

        // 启动异步服务器
        asyncServer.setService(jobService);
        asyncServer.startup();
    }
}

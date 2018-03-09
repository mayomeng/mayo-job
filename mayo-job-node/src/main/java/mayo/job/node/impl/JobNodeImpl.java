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
    private int nodeId; // 节点ID

    @Autowired
    private JobCoordinate jobCoordinate;
    @Autowired
    @Qualifier("JobServiceContainer")
    private JobService jobService;
    @Autowired
    private JobServer jobServer;
    @Autowired
    private JobEnvironment jobEnvironment;

    @Override
    public void startup() {
        jobEnvironment.setNodeId(nodeId);
        jobEnvironment.setRole(jobCoordinate.getRole());
        jobCoordinate.election();
        jobCoordinate.monitor();
        jobServer.setService(jobService);
        jobServer.startup();
    }

    @Override
    public JobEnvironment getJobEnvironment() {
        return jobEnvironment;
    }
}

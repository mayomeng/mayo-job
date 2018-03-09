package mayo.job.node;

import lombok.Getter;
import lombok.Setter;
import mayo.job.node.coordinate.JobCoordinate;
import mayo.job.parent.node.JobNode;
import mayo.job.parent.server.JobServer;
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
@Getter
@Setter
public class JobNodeImpl implements JobNode {
    private int nodeId; // 节点ID
    private String host; // 节点IP
    private int port; // 节点端口

    @Autowired
    private JobCoordinate jobCoordinate;
    @Autowired
    @Qualifier("JobServiceContainer")
    private JobService jobService;
    @Autowired
    private JobServer jobServer;

    @Override
    public void startup() {
        jobCoordinate.election();
        jobCoordinate.monitor();
        jobServer.setService(jobService);
        jobServer.startup();
    }
}

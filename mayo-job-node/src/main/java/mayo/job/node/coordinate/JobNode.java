package mayo.job.node.coordinate;

import lombok.Getter;
import lombok.Setter;
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
public class JobNode {
    private int nodeId; // 节点ID
    private String role; // 节点角色
    private String host; // 节点IP
    private int port; // 节点端口
}

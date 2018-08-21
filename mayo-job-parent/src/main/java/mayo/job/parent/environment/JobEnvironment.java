package mayo.job.parent.environment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 任务运行环境情报
 */
@PropertySource("classpath:job.properties")
@ConfigurationProperties(prefix = "server")
@Getter
@Setter
@ToString
@Component
@Slf4j
public class JobEnvironment {
    private String host;
    private int port;
    private String nodeId;
    private String nodeType;
    private String nodeRole;
    private List<String> jobList;
    private int connectCount; // 当前连接数(同步任务用)

    @PostConstruct
    public void init() {
        String nodeId = System.getenv("nodeId");
        if (StringUtils.isNotEmpty(nodeId)) {
            this.nodeId = nodeId;
        }
        log.info("the nodeId is : {}", this.nodeId);
        String host = System.getenv("host");
        if (StringUtils.isNotEmpty(host)) {
            this.host = host;
        }
        log.info("the host is : {}", this.host);
        String port = System.getenv("port");
        if (StringUtils.isNotEmpty(port)) {
            this.port = Integer.parseInt(port);
        }
        log.info("the port is : {}", this.port);
    }
}

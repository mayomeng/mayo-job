package mayo.job.config.zookeeper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * zookeeper配置文件
 */
@ConfigurationProperties(prefix = "zookeeper")
@PropertySource("classpath:zookeeper.properties")
@Primary
@Component
@Getter
@Setter
public class ZookeeperProperties {
    private String connections; // ip:host
    private int sessionTimeout; // session过期时间
    private int connectionTimeout; // 连接超时

    private int retryBaseSleepTimeMs;
    private int retryMaxRetries;

    private String namespace;
    private String leaderPath;
    private String executerPath;
    private String dispatchPath;

    // 整个项目命名空间根节点的权限
    private String platformAclScheme;
    private String adminAuth;
}

package mayo.job.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 服务器配置类.
 */
@ConfigurationProperties(prefix = "server")
@PropertySource("classpath:server.properties")
@Component
@Getter
@Setter
public class JobServerProperties {
    private String host;
    private int port;
    private int accpectThreadNum;
    private int workThreadNum;
}

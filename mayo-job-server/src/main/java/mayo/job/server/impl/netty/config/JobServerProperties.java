package mayo.job.server.impl.netty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 处理服务信息配置类.
 */
@PropertySource("classpath:server.properties")
@Component
public class JobServerProperties {
    public String Host;
    public int Port;
    public int AccpectCount;
    public int WorkCount;
}

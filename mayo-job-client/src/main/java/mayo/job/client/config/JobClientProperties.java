package mayo.job.client.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 客户端配置类.
 */
@PropertySource("classpath:client.properties")
@ConfigurationProperties(prefix = "mayo/job/client")
@Setter
@Component
public class JobClientProperties {
    public int ClientCount;
    public int ClientThreadCount;
}

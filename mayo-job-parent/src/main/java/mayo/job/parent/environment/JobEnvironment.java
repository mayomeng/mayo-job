package mayo.job.parent.environment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务运行环境情报
 */
@PropertySource("classpath:job.properties")
@ConfigurationProperties(prefix = "node")
@Getter
@Setter
@ToString
@Component
public class JobEnvironment {
    private String host;
    private String nodeId;
    private String nodeType;
    private String nodeRole;
    private List<String> jobList;
}

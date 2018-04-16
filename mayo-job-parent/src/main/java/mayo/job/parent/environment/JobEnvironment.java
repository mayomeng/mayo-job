package mayo.job.parent.environment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务运行环境情报
 */
@Getter
@Setter
@ToString
@Component
public class JobEnvironment {
    private String nodeId;
    private String nodeType;
    private String host;
    private int port;
    private String nodeRole;
    private List<String> jobList;
}

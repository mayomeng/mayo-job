package mayo.job.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 任务服务器配置.
 */
@PropertySource("classpath:job.properties")
@ConfigurationProperties(prefix = "server")
@Getter
@Setter
@Component
public class JobServerProperties {
    private String host; // IP
    private int port; // 端口
    private int backlog; // 最大连接数(还需要修改服务器的somaxconn)
    private int acceptCount; // 响应线程数
    private int workCount; // 工作线程数
    private String protocol; // 协议
    private int allotCount; // 每次分配任务数量
    private int allotTime; // 每隔多久分配一次任务(秒)
    private int pullCount; // 异步执行每次拉取任务数量
    private int pullTime; // 每隔多久拉取一次任务(秒)
}

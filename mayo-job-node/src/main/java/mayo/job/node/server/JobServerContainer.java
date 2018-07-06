package mayo.job.node.server;

import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.enums.NodeTypeEnum;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.parent.service.JobService;
import mayo.job.server.JobServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 服务器容器.
 */
@Component
@Primary
@Slf4j
public class JobServerContainer implements JobServer {

    @Autowired
    @Qualifier("syncServer")
    private JobServer syncServer;
    @Autowired
    @Qualifier("asyncServer")
    private JobServer asyncServer;
    @Autowired
    private JobEnvironment jobEnvironment;
    @Autowired
    @Qualifier("JobServiceContainer")
    private JobService jobService;

    @Override
    public void setService(JobService jobService) {
    }

    @Override
    public void startup() throws Exception {
        if (NodeTypeEnum.TYPE_SYNC.VALUE.equals(jobEnvironment.getNodeType())) { // 启动同步服务器
            startupServer(syncServer);
        } else if (NodeTypeEnum.TYPE_ASYNC.VALUE.equals(jobEnvironment.getNodeType())) { // 启动异步服务器
            startupServer(asyncServer);
        } else { // 同时启动同步，异步服务器
            startupServer(syncServer);
            startupServer(asyncServer);
        }
    }

    /**
     * 启动服务器
     */
    private void startupServer(JobServer jobServer) {
        new Thread(() -> {
            jobServer.setService(jobService);
            try {
                jobServer.startup();
            } catch (Exception e) {
                jobServer.shutdown();
                log.error(e.getMessage(), e);
            }
        }).start();
    }

    @Override
    public void shutdown() {
        if (NodeTypeEnum.TYPE_SYNC.VALUE.equals(jobEnvironment.getNodeType())) { // 启动同步服务器
            syncServer.shutdown();
        } else if (NodeTypeEnum.TYPE_ASYNC.VALUE.equals(jobEnvironment.getNodeType())) { // 启动异步服务器
            asyncServer.shutdown();
        } else { // 同时启动同步，异步服务器
            syncServer.shutdown();
            asyncServer.shutdown();
        }
    }
}

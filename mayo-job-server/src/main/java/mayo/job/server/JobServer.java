package mayo.job.server;

import mayo.job.parent.service.JobService;
import org.quartz.SchedulerException;

/**
 * 任务服务器接口.
 */
public interface JobServer {
    /**
     * 设置任务处理逻辑.
     */
    void setService(JobService jobService);
    /**
     * 启动服务器.
     */
    void startup() throws Exception;
    /**
     * 关闭服务器.
     */
    void shutdown();
}

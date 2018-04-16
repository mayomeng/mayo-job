package mayo.job.server.async;

/**
 * 任务异步服务器
 */
public interface JobAsyncServer {
    /**
     * 启动
     */
    void startUp();
    /**
     * 终止
     */
    void shutDown();
}

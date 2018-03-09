package mayo.job.parent.service;

/**
 * 任务服务
 */
public interface JobService {
    /**
     * 初始化.
     */
    void init();
    /**
     * 返回服务任务类型.
     */
    String getJobType();
    /**
     * 执行任务.
     */
    Object execute(Object param);
}

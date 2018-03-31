package mayo.job.parent.service;

import java.util.List;

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
     * 返回服务支持任务名
     */
    List<String> getJobNameList();
    /**
     * 执行任务.
     */
    Object execute(Object param);
}

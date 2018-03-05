package mayo.job.node;

/**
 * 任务节点接口
 */
public interface JobNode {
    void init();
    String getJobType();
}

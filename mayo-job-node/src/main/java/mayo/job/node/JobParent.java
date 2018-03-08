package mayo.job.node;

/**
 * 任务节点接口
 */
public interface JobParent {
    void init();
    String getJobType();
}

package mayo.job.node.coordinate;

/**
 * 任务协调器，负责调度器选举，监控
 */
public interface JobCoordinate {
    /**
     * 进行调度器选举
     */
    public void init();
    /**
     * 返回角色
     */
    public String getRole();
    /**
     * 进行调度器选举
     */
    public void election();

    /**
     * 监控
     */
    public void monitor();
}

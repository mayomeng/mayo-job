package mayo.job.parent.control;

/**
 * 限流开关.
 */
public interface LimitSwitch {
    /**
     * 取得限流处理任务数(每秒).
     */
    Integer getLimitRate();
}

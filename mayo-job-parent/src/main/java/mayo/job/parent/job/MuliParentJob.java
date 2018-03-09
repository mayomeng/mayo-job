package mayo.job.parent.job;

/**
 * 并发任务(父任务)
 */
public class MuliParentJob extends JobAdapter implements Job {
    protected int jobCount; // 任务个数
    @Override
    public String getJobType() {
        return null;
    }
}

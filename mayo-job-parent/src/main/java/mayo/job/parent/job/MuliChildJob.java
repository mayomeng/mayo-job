package mayo.job.parent.job;

/**
 * 并发任务(子任务)
 */
public class MuliChildJob extends JobAdapter implements Job {
    protected long parentJobId; //父任务ID
    @Override
    public String getJobType() {
        return null;
    }
}

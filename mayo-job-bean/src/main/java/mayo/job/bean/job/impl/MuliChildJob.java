package mayo.job.bean.job.impl;
import mayo.job.bean.job.Job;
import mayo.job.bean.job.JobAdapter;

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

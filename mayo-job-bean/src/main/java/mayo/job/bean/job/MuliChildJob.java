package mayo.job.bean.job;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

/**
 * 并发任务(子任务)
 */
public class MuliChildJob implements Job {
    protected JobResult result; // 任务执行结果
    protected long parentJobId; //父任务ID

    @Override
    public JobResult handle(JobParam jobParam) {
        return null;
    }

    @Override
    public String getJobType() {
        return null;
    }
}

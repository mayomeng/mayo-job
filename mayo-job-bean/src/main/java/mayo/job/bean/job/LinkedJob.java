package mayo.job.bean.job;

import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

/**
 * 链式任务
 */
public class LinkedJob implements Job {
    protected long nextJobId; // 下一个任务ID
    protected int jobCount; // 任务个数

    @Override
    public JobResult handle(JobParam jobParam) {
        return null;
    }

    @Override
    public String getJobType() {
        return null;
    }
}

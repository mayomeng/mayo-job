package mayo.job.bean.job;

import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

/**
 * 定时任务
 */
public class TimingJob implements Job {
    protected String cron; // 任务执行周期

    @Override
    public JobResult handle(JobParam jobParam) {
        return null;
    }

    @Override
    public String getJobType() {
        return null;
    }
}

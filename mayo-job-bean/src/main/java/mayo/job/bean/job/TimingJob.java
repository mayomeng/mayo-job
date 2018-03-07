package mayo.job.bean.job;

import mayo.job.bean.enums.JobTypeEnum;
import mayo.job.bean.job.Job;
import mayo.job.bean.job.JobAdapter;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

/**
 * 定时任务
 */
public class TimingJob extends JobAdapter implements Job {
    protected String cron; // 任务执行周期
    @Override
    public String getJobType() {
        return JobTypeEnum.TIMING_JOB.VALUE;
    }
}

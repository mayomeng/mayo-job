package mayo.job.parent.job;

import mayo.job.parent.enums.JobTypeEnum;

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

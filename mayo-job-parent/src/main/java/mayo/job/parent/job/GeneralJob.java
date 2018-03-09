package mayo.job.parent.job;

import mayo.job.parent.enums.JobTypeEnum;

/**
 * 普通任务.
 */
public class GeneralJob extends JobAdapter implements Job {
    @Override
    public String getJobType() {
        return JobTypeEnum.GENERAL_JOB.VALUE;
    }
}

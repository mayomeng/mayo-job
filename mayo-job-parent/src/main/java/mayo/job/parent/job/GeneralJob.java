package mayo.job.parent.job;

import mayo.job.parent.enums.JobTypeEnum;

/**
 * 普通任务.
 */
public class GeneralJob extends JobAdapter {
    @Override
    protected void setJobType() {
        addJobType(JobTypeEnum.GENERAL_JOB.VALUE);
    }
}

package mayo.job.parent.job;

import mayo.job.parent.enums.JobTypeEnum;

/**
 * 并发任务(父任务)
 */
public class MuliParentJob extends JobAdapter {
    protected int jobCount; // 任务个数

    @Override
    protected void setJobType() {
        addJobType(JobTypeEnum.MULI_JOB.VALUE);
    }
}

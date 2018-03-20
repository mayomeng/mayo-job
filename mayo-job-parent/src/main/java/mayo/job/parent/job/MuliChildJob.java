package mayo.job.parent.job;

import mayo.job.parent.enums.JobTypeEnum;

/**
 * 并发任务(子任务)
 */
public class MuliChildJob extends JobAdapter {
    protected long parentJobId; //父任务ID

    @Override
    protected void setJobType() {
        addJobType(JobTypeEnum.MULI_JOB.VALUE);
    }
}

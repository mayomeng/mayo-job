package mayo.job.parent.job;

import mayo.job.parent.enums.JobTypeEnum;

/**
 * 链式任务
 */
public class LinkedJob extends JobAdapter {
    protected long nextJobId; // 下一个任务ID
    protected int jobCount; // 任务个数

    @Override
    protected void setJobType() {
        addJobType(JobTypeEnum.LINKED_JOB.VALUE);
    }
}

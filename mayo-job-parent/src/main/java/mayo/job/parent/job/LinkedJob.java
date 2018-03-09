package mayo.job.parent.job;

import mayo.job.parent.enums.JobTypeEnum;

/**
 * 链式任务
 */
public class LinkedJob extends JobAdapter implements Job {
    protected long nextJobId; // 下一个任务ID
    protected int jobCount; // 任务个数
    @Override
    public String getJobType() {
        return JobTypeEnum.LINKED_JOB.VALUE;
    }
}

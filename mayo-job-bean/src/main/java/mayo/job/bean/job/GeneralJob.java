package mayo.job.bean.job;

import mayo.job.bean.enums.JobTypeEnum;
import mayo.job.bean.job.Job;
import mayo.job.bean.job.JobAdapter;

/**
 * 普通任务.
 */
public class GeneralJob extends JobAdapter implements Job {
    @Override
    public String getJobType() {
        return JobTypeEnum.GENERAL_JOB.VALUE;
    }
}

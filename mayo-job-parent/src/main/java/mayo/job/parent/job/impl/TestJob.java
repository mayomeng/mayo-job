package mayo.job.parent.job.impl;

import mayo.job.parent.annotation.JobMark;
import mayo.job.parent.annotation.LimitMark;
import mayo.job.parent.enums.JobTypeEnum;
import mayo.job.parent.job.JobAdapter;
import mayo.job.parent.param.JobParam;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 测试用任务类.
 */
@JobMark(jobName = "Test")
@LimitMark(limitRate = 10, limitTimeOut=100)
@Component
public class TestJob extends JobAdapter {

    @Override
    protected void setJobType() {
        addJobType(JobTypeEnum.GENERAL_JOB.VALUE);
    }

    @Override
    public JobParam handleJob(JobParam jobParam) {
        jobParam.setSuccess(true);
        jobParam.setResult(jobParam.getParams());
        jobParam.setEndTime(new Date());
        return jobParam;
    }
}

package mayo.job.parent.job.impl;

import mayo.job.parent.JobMark;
import mayo.job.parent.job.GeneralJob;
import mayo.job.parent.param.JobParam;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 测试用任务类.
 */
@JobMark(jobName = "Test")
@Component
public class TestJob extends GeneralJob {

    public JobParam handle(JobParam jobParam) {
        jobParam.setSuccess(true);
        jobParam.setResult(jobParam.getParams());
        jobParam.setEndTime(new Date());
        return jobParam;
    }
}

package mayo.job.parent.job.impl;

import mayo.job.parent.job.GeneralJob;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;

import java.util.Date;

/**
 * Created by SKJ-05A14-0049 on 2018/3/6.
 */
public class TestJob extends GeneralJob {

    public JobResult handle(JobParam jobParam) {
        JobResult jobResult = new JobResult();
        jobResult.setJobId(jobParam.getJobId());
        jobResult.setResult(jobParam.getParams());
        jobResult.setEndTime(new Date());
        return jobResult;
    }

    public String getJobName() {
        return "Test";
    }
}

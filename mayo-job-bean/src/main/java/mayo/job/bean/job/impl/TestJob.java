package mayo.job.bean.job.impl;

import mayo.job.bean.job.GeneralJob;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

import java.util.Date;

/**
 * Created by SKJ-05A14-0049 on 2018/3/6.
 */
public class TestJob extends GeneralJob {

    public JobResult handle(JobParam jobParam) {
        JobResult jobResult = new JobResult();
        jobResult.setJobId(100001);
        jobResult.setResult(jobParam.getParams());
        jobResult.setEndTime(new Date());
        return jobResult;
    }
    public String getJobName() {
        return "Test";
    }
}

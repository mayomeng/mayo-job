package mayo.job.parent.job.impl;

import mayo.job.parent.job.GeneralJob;
import mayo.job.parent.param.JobParam;

import java.util.Date;

/**
 * Created by SKJ-05A14-0049 on 2018/3/6.
 */
public class TestJob extends GeneralJob {

    public JobParam handle(JobParam jobParam) {
        jobParam.setResult(jobParam.getParams());
        jobParam.setEndTime(new Date());
        return jobParam;
    }

    public String getJobName() {
        return "Test";
    }
}

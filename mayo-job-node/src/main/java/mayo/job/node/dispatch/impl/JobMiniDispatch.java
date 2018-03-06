package mayo.job.node.dispatch.impl;

import mayo.job.bean.enums.JobTypeEnum;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;
import mayo.job.node.JobDict;
import mayo.job.node.dispatch.JobDispatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 最小连接调度器（将任务分配给任务数最少的执行器）.
 */
@Component
public class JobMiniDispatch implements JobDispatch {

    @Autowired
    private JobDict jobDict;

    @Override
    public JobResult dispatch(JobParam jobParam) {
        return null;
    }

    @Override
    @PostConstruct
    public void init() {
        jobDict.put(getJobType(), this);
    }

    @Override
    public String getJobType() {
        return JobTypeEnum.GENERAL_JOB.VALUE;
    }
}

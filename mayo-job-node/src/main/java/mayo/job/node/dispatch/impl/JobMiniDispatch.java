package mayo.job.node.dispatch.impl;

import mayo.job.parent.enums.JobTypeEnum;
import mayo.job.node.service.JobDict;
import mayo.job.node.dispatch.JobDispatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 最小连接调度器（将任务分配给任务数最少的执行器）.
 */
@Component
public class JobMiniDispatch implements JobDispatch {

    @Autowired
    private JobDict jobDict;

    @Override
    @PostConstruct
    public void init() {
        jobDict.put(getJobType(), this);
    }

    @Override
    public String getJobType() {
        return JobTypeEnum.GENERAL_JOB.VALUE;
    }

    @Override
    public List<String> getJobNameList() {
        return null;
    }

    @Override
    public Object execute(Object param) {
        return null;
    }
}

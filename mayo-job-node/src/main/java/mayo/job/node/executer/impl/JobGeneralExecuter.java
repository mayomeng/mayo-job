package mayo.job.node.executer.impl;

import mayo.job.parent.enums.JobTypeEnum;
import mayo.job.parent.job.Job;
import mayo.job.parent.param.JobParam;
import mayo.job.node.service.JobDict;
import mayo.job.node.executer.JobExecuter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 普通任务执行器
 */
@Component
public class JobGeneralExecuter implements JobExecuter {

    @Autowired
    private JobDict jobDict;
    private Map<String, Job> jobMap;
    private List<String> jobNameList;

    @Override
    public Object execute(Object param) {
        JobParam jobParam = (JobParam)param;
        return jobMap.get(jobParam.getJobName()).handle(jobParam);
    }

    @Override
    @PostConstruct
    public void init() {
        jobDict.put(getJobType(), this);
        jobMap = new HashMap<>();
        ServiceLoader<Job> jobs = ServiceLoader.load(Job.class);
        jobs.forEach(job -> {
            if (StringUtils.isNoneEmpty(job.getJobName())) {
                jobMap.put(job.getJobName(), job);
                jobDict.put(job.getJobName(), getJobType());
                jobNameList.add(job.getJobName());
            }
        });
    }

    @Override
    public String getJobType() {
        return JobTypeEnum.GENERAL_JOB.VALUE;
    }

    @Override
    public List<String> getJobNameList() {
        return jobNameList;
    }
}

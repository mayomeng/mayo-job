package mayo.job.node.executer.impl;

import mayo.job.bean.enums.JobTypeEnum;
import mayo.job.bean.job.Job;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;
import mayo.job.node.JobDict;
import mayo.job.node.executer.JobExecuter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
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

    @Override
    public JobResult execute(JobParam param) {
        return jobMap.get(param.getJobName()).handle(param);
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
            }
        });
    }

    @Override
    public String getJobType() {
        return JobTypeEnum.GENERAL_JOB.VALUE;
    }
}

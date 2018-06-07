package mayo.job.node.executer.impl;

import mayo.job.parent.JobMark;
import mayo.job.parent.common.SpringContext;
import mayo.job.parent.enums.JobTypeEnum;
import mayo.job.parent.job.Job;
import mayo.job.parent.param.JobParam;
import mayo.job.node.service.JobDict;
import mayo.job.node.executer.JobExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 普通任务执行器
 */
@Component
@ConditionalOnBean(name={"springContext"})
public class JobGeneralExecuter implements JobExecuter {

    @Autowired
    private JobDict jobDict;
    private Map<String, Job> jobMap;
    private List<String> jobNameList;

    @Autowired
    private SpringContext springContext;

    @Override
    public Object execute(Object param) {
        JobParam jobParam = (JobParam)param;
        return jobMap.get(jobParam.getJobName()).handle(jobParam);
    }

    @Override
    @PostConstruct
    public void init() {
        jobDict.put(getJobType(), this);
        Map<String, Object> jobInstanceMap = springContext.getBeansWithAnnotation(JobMark.class);
        jobMap = new HashMap<>(jobInstanceMap.size());
        jobNameList = new ArrayList<>();
        jobInstanceMap.entrySet().forEach(entity -> {
            Job job = (Job)entity.getValue();
            JobMark jobMark = job.getClass().getAnnotation(JobMark.class);
            jobMap.put(jobMark.jobName(), job);
            jobDict.put(jobMark.jobName(), getJobType());
            jobNameList.add(jobMark.jobName());
        });
/*        ServiceLoader<Job> jobs = ServiceLoader.load(Job.class);
        jobs.forEach(job -> {
            if (StringUtils.isNoneEmpty(job.getJobName())) {
                jobMap.put(job.getJobName(), job);
                jobDict.put(job.getJobName(), getJobType());
                jobNameList.add(job.getJobName());
            }
        });*/
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

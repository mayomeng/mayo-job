package mayo.job.server.async.disruptor.strategy;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.server.JobServerProperties;
import mayo.job.server.async.disruptor.assembly.JobDisruptorPublisher;
import mayo.job.store.AsyncJobStorer;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 任务分配策略
 */
@Component
@Slf4j
public class JobAllotStrategy {

    @Autowired
    private JobServerProperties jobServerProperties;
    @Autowired
    private AsyncJobStorer asyncJobStorer;
    @Autowired
    private JobEnvironment jobEnvironment;
    @Autowired
    private JobDisruptorPublisher jobDisruptorPublisher;
    @Autowired
    private Scheduler scheduler;
    @Setter
    private List<String> jobNameList;

    /**
     * 分配任务
     */
    @PostConstruct
    public void startup() throws Exception {
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
/*        jobDetailFactoryBean.setName("allot_job");
        jobDetailFactoryBean.setGroup("allot_job_group");*/
        jobDetailFactoryBean.setTargetObject(this);
        jobDetailFactoryBean.setTargetMethod("allot");
        jobDetailFactoryBean.setConcurrent(false);
        jobDetailFactoryBean.afterPropertiesSet();

        SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();
/*        triggerFactoryBean.setName("allot_trigger");
        triggerFactoryBean.setGroup("allot_trigger_group");*/
        triggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        triggerFactoryBean.setRepeatInterval(jobServerProperties.getAllotTime()*1000);
        triggerFactoryBean.afterPropertiesSet();

        scheduler.scheduleJob(jobDetailFactoryBean.getObject(), triggerFactoryBean.getObject());
    }

    /**
     * 分配任务
     */
    public void allot() {
        if (!jobDisruptorPublisher.isStartup()) {
            return;
        }
        jobNameList.forEach(jobName -> {
            if (asyncJobStorer.getPendingJobCount(jobEnvironment.getNodeId(), jobName) == 0) {
                asyncJobStorer.allotJob(jobEnvironment.getNodeId(), jobName, jobServerProperties.getAllotCount());
            }
        });
    }
}

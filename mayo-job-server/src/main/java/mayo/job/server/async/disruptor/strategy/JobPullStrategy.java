package mayo.job.server.async.disruptor.strategy;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.parent.param.JobParam;
import mayo.job.server.JobServerProperties;
import mayo.job.server.async.disruptor.assembly.JobDisruptorPublisher;
import mayo.job.store.AsyncJobStorer;
import org.dozer.Mapper;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 任务拉取策略类
 */
@Component
@Slf4j
public class JobPullStrategy {

    @Autowired
    private JobServerProperties jobServerProperties;
    @Autowired
    private AsyncJobStorer asyncJobStorer;
    @Autowired
    private JobEnvironment jobEnvironment;
    @Autowired
    private Mapper mapper;
    @Autowired
    private JobDisruptorPublisher jobDisruptorPublisher;
    @Autowired
    private Scheduler scheduler;
    @Setter
    private List<String> jobNameList;

    /**
     * 拉取任务
     */
    @PostConstruct
    public void startup() throws Exception {
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactoryBean.setTargetObject(this);
        jobDetailFactoryBean.setTargetMethod("pullJobParam");
        jobDetailFactoryBean.setConcurrent(false);
        jobDetailFactoryBean.afterPropertiesSet();

        SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        triggerFactoryBean.setRepeatInterval(jobServerProperties.getPullTime()*1000);
        triggerFactoryBean.afterPropertiesSet();

        scheduler.scheduleJob(jobDetailFactoryBean.getObject(), triggerFactoryBean.getObject());
    }

    /**
     * 从任务存储节点批量拉取任务
     */
    public void pullJobParam() {
        if (!jobDisruptorPublisher.isStartup()) {
            return;
        }
        jobNameList.forEach(jobName -> {
            if (isPullAble(jobName)) {
                List<Object> jobParamList = asyncJobStorer.pullMultipleJob(jobEnvironment.getNodeId(), jobName, jobServerProperties.getPullCount());
                if (!CollectionUtils.isEmpty(jobParamList)) {
                    jobParamList.forEach(item -> {
                        JobParam jobParam = mapper.map(item, JobParam.class);
                        jobParam.setNodeId(jobEnvironment.getNodeId());
                        jobDisruptorPublisher.publish(jobParam);
                    });
                }
            }
        });
    }

    /**
     * 是否可以拉取任务
     */
    private boolean isPullAble(String jobName) {
        boolean isPullAble = false;
        if (jobDisruptorPublisher.getRingBuffer().getCursor() - jobDisruptorPublisher.getSubscribeBarrier().getCursor()  < jobServerProperties.getBacklog()/2) {
            isPullAble = true;
        }
        return isPullAble;
    }
}

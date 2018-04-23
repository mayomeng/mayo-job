package mayo.job.config.quartz;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 定时任务工厂类
 */
@Component
public class QuartzJobExecutor {
    @Autowired
    private Scheduler scheduler;

    /**
     * 提交定时任务
     */
    public void submit(Object targetObject, String targetMethod, long repeatInterval) throws Exception {
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactoryBean.setTargetObject(targetObject);
        jobDetailFactoryBean.setTargetMethod(targetMethod);
        jobDetailFactoryBean.setConcurrent(false);
        jobDetailFactoryBean.afterPropertiesSet();

        SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        triggerFactoryBean.setRepeatInterval(repeatInterval);
        triggerFactoryBean.afterPropertiesSet();

        scheduler.scheduleJob(jobDetailFactoryBean.getObject(), triggerFactoryBean.getObject());
    }
}

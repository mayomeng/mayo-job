package mayo.job.server.async.disruptor.strategy;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.config.quartz.QuartzJobExecutor;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.server.JobServerProperties;
import mayo.job.server.async.disruptor.assembly.JobDisruptorPublisher;
import mayo.job.store.AsyncJobStorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 任务分配策略（将任务分配给本执行器）
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
    private QuartzJobExecutor quartzJobExecutor;
    @Setter
    private List<String> jobNameList;

    /**
     * 分配任务
     */
    @PostConstruct
    public void startup() throws Exception {
        quartzJobExecutor.submit(this, "allot", jobServerProperties.getAllotTime());
    }

    /**
     * 分配任务
     */
    public void allot() {
        if (!jobDisruptorPublisher.isStartup()) {
            return;
        }
        jobNameList.forEach(jobName -> {
            if (asyncJobStorer.getPendingJobCount(jobEnvironment.getNodeId(), jobName) < jobServerProperties.getAllotCount()) {
                asyncJobStorer.allotJob(jobEnvironment.getNodeId(), jobName, jobServerProperties.getAllotCount());
            }
        });
    }
}

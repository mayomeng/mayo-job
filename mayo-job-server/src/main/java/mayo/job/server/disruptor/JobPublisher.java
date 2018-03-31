package mayo.job.server.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.service.JobService;
import mayo.job.server.JobServer;
import mayo.job.server.JobServerProperties;
import mayo.job.store.AsyncJobStorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务发布者
 */
@Component
@Slf4j
public class JobPublisher {

    @Autowired
    private JobServerProperties jobServerProperties;
    @Autowired
    private AsyncJobStorer asyncJobStorer;
    @Autowired
    private JobEnvironment jobEnvironment;

    @Getter
    private RingBuffer<JobParam> ringBuffer;

    private ExecutorService acceptThreadPool;
    private volatile boolean isStartup = true;

    private JobService jobService;

    private static class JobEventFactory implements EventFactory<JobParam> {
        @Override
        public JobParam newInstance() {
            return new JobParam();
        }
    }

    @PostConstruct
    public void init() {
        JobEventFactory jobEventFactory = new JobEventFactory();
        ringBuffer = RingBuffer.createMultiProducer(jobEventFactory, jobServerProperties.getBacklog(),
                new YieldingWaitStrategy());
        acceptThreadPool = Executors.newFixedThreadPool(jobServerProperties.getAcceptCount());
    }

    private static final EventTranslatorOneArg<JobParam, JobParam> TRANSLATOR = new EventTranslatorOneArg<JobParam, JobParam>() {
        public void translateTo(JobParam event, long sequence, JobParam param) {
            event = param;
        }
    };

    /**
     * 向环形缓冲区中放入任务参数
     */
    public void startup(JobService jobService) {
        this.jobService = jobService;
        for (int i = 0 ; i < jobServerProperties.getAcceptCount() ; i++) {
            acceptThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    for (;isStartup;) {
                        if (isPullAble()) {
                            pullJobParam();
                        }
                    }
                }
            });
        }
    }

    /**
     * 从任务存储节点批量拉取任务
     */
    private void pullJobParam() {
        jobService.getJobNameList().forEach(jobName -> {
            asyncJobStorer.allotJob(jobEnvironment.getNodeId(), jobName, jobServerProperties.getPullCount());
            asyncJobStorer.prepareJob(jobEnvironment.getNodeId(), jobName, jobServerProperties.getPullCount());
            List<JobParam> jobParamList = asyncJobStorer.pullMultipleJob(jobEnvironment.getNodeId(), jobName);
            log.debug("拉取任务个数{}.", jobParamList.size());
            jobParamList.forEach(jobParam ->  {
                ringBuffer.publishEvent(TRANSLATOR, jobParam);
            });
        });
    }

    /**
     * 是否可以拉取任务
     */
    private boolean isPullAble() {
        // TODO
        return true;
    }

    /**
     * 销毁发布者
     */
    public void shutdown() {
        isStartup = false;
        acceptThreadPool.shutdown();
    }
}

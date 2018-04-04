package mayo.job.server.disruptor;

import com.lmax.disruptor.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.parent.param.JobParam;
import mayo.job.server.JobServerProperties;
import mayo.job.store.AsyncJobStorer;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
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
    @Autowired
    private Mapper mapper;

    @Getter
    private RingBuffer<JobParam> ringBuffer;

    private ExecutorService acceptThreadPool;
    private volatile boolean isStartup = true;

    private List<String> jobNameList;
    private SequenceBarrier subscribeBarrier;

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
            event.setJobId(param.getJobId());
            event.setJobName(param.getJobName());
            event.setParams(param.getParams());
        }
    };

    /**
     * 向环形缓冲区中放入任务参数
     */
    public void startup(List<String> jobNameList, SequenceBarrier subscribeBarrier) {
        this.jobNameList = jobNameList;
        this.subscribeBarrier = subscribeBarrier;
        for (int i = 0 ; i < jobServerProperties.getAcceptCount() ; i++) {
            acceptThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    for (;isStartup;) {
                        if (isPullAble()) {
                            clearCompleteJob();
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
        jobNameList.forEach(jobName -> {
            asyncJobStorer.allotJob(jobEnvironment.getNodeId(), jobName, jobServerProperties.getPullCount());
            asyncJobStorer.prepareJob(jobEnvironment.getNodeId(), jobName, jobServerProperties.getPullCount());
            List<Object> jobParamList = asyncJobStorer.pullMultipleJob(jobEnvironment.getNodeId(), jobName);
            //log.debug("拉取任务个数{}.", jobParamList.size());
            jobParamList.forEach(jobParam -> {
                ringBuffer.publishEvent(TRANSLATOR, mapper.map(jobParam, JobParam.class));
            });
        });
    }

    /**
     * 清除执行完毕的任务
     */
    private void clearCompleteJob() {
        jobNameList.forEach(jobName -> {
           asyncJobStorer.clearHandlingJobList(jobEnvironment.getNodeId(), jobName);
        });
    }

    /**
     * 是否可以拉取任务
     */
    private boolean isPullAble() {
        // TODO 需要测试
        boolean isPullAble = false;
        if (ringBuffer.getCursor() == subscribeBarrier.getCursor()) {
            isPullAble = true;
        }
        return isPullAble;
    }

    /**
     * 销毁发布者
     */
    public void shutdown() {
        isStartup = false;
        acceptThreadPool.shutdown();
    }
}

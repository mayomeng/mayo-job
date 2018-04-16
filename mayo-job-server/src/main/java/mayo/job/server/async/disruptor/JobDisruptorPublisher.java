package mayo.job.server.async.disruptor;

import com.lmax.disruptor.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.param.JobParam;
import mayo.job.server.JobServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 任务发布者
 */
@Component
@Slf4j
public class JobDisruptorPublisher {

    @Autowired
    private JobServerProperties jobServerProperties;

    @Getter
    private RingBuffer<JobParam> ringBuffer;
    @Getter
    private SequenceBarrier subscribeBarrier;

    @Getter
    private volatile boolean isStartup = false;

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
        subscribeBarrier = ringBuffer.newBarrier();
    }

    private static final EventTranslatorOneArg<JobParam, JobParam> TRANSLATOR = new EventTranslatorOneArg<JobParam, JobParam>() {
        public void translateTo(JobParam event, long sequence, JobParam param) {
            event.setJobId(param.getJobId());
            event.setJobName(param.getJobName());
            event.setParams(param.getParams());
        }
    };

    /**
     * 开始拉取任务
     */
    public void startup() {
        isStartup = true;
    }

    /**
     * 从任务存储节点批量拉取任务
     */
    public void publish(JobParam jobParam) {
        ringBuffer.publishEvent(TRANSLATOR, jobParam);
    }

    /**
     * 销毁发布者
     */
    public void shutdown() {
        isStartup = false;
    }
}

package mayo.job.server.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import lombok.Getter;
import mayo.job.parent.param.JobParam;
import mayo.job.server.JobServerProperties;
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
public class JobPublisher {

    @Autowired
    private JobServerProperties jobServerProperties;

    @Getter
    private RingBuffer<JobParam> ringBuffer;

    private ExecutorService acceptThreadPool;
    private volatile boolean isStartup = true;

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
    public void startup() {
        for (int i = 0 ; i < jobServerProperties.getAcceptCount() ; i++) {
            acceptThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    for (;isStartup;) {
                        pullJobParam().forEach(jobParam ->  {
                            ringBuffer.publishEvent(TRANSLATOR, jobParam);
                        });
                    }
                }
            });
        }
    }

    /**
     * 从任务存储节点批量拉取任务
     */
    private List<JobParam> pullJobParam() {
        // TODO
        return new ArrayList<>();
    }

    /**
     * 销毁发布者
     */
    public void shutdown() {
        isStartup = false;
        acceptThreadPool.shutdown();
    }
}

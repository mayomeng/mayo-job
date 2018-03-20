package mayo.job.server.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.service.JobService;
import mayo.job.server.JobServer;
import mayo.job.server.JobServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * disruptor实现的job服务器(处理异步请求)
 */
@Component("asyncServer")
@Slf4j
public class JobDisruptorServer implements JobServer {

    @Autowired
    private JobServerProperties jobServerProperties;

    private JobService jobService;

    @Override
    public void setService(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {
        RingBuffer<JobParam> ringBuffer = RingBuffer.createMultiProducer(
                new EventFactory<JobParam>() {
                    @Override
                    public JobParam newInstance() {
                        return null;
                    }
                }, jobServerProperties.getBacklog(), new YieldingWaitStrategy());
    }
}

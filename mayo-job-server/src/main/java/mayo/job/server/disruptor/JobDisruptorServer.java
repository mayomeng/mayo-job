package mayo.job.server.disruptor;

import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.service.JobService;
import mayo.job.server.JobServer;
import mayo.job.server.JobServerProperties;
import mayo.job.store.AsyncJobStorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * disruptor实现的job服务器(处理异步请求)
 */
@Component("asyncServer")
@Slf4j
public class JobDisruptorServer implements JobServer {

    @Autowired
    private JobServerProperties jobServerProperties;

    private JobService jobService;

    @Autowired
    private JobPublisher jobPublisher;

    @Autowired
    private AsyncJobStorer asyncJobStorer;

    private ExecutorService workThreadPool;

    private WorkerPool<JobParam> workerPool;

    @Override
    public void setService(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * 启动异步服务器
     */
    @Override
    public void startup() {
        workThreadPool = Executors.newFixedThreadPool(jobServerProperties.getWorkCount());
        SequenceBarrier subscribeBarrier = jobPublisher.getRingBuffer().newBarrier();
        JobSubscriber[] jobSubscribers = new JobSubscriber[jobServerProperties.getWorkCount()];
        for (int i = 0 ; i < jobServerProperties.getWorkCount() ; i++) {
            jobSubscribers[i] = new JobSubscriber(jobService, asyncJobStorer);
        }
        workerPool = new WorkerPool<>(jobPublisher.getRingBuffer(),
                subscribeBarrier, new IgnoreExceptionHandler(), jobSubscribers);
        Sequence[] sequences = workerPool.getWorkerSequences();
        jobPublisher.getRingBuffer().addGatingSequences(sequences);
        workerPool.start(workThreadPool);
        jobPublisher.startup(jobService.getJobNameList(), subscribeBarrier);
        log.info("the asyncJobServer is run.");
    }

    /**
     * 停止异步服务器
     */
    @Override
    public void shutdown() {
        jobPublisher.shutdown();
        workerPool.halt();
        workThreadPool.shutdown();
    }
}

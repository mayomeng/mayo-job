package mayo.job.server.async.disruptor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.WorkerPool;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.service.JobService;
import mayo.job.server.JobServer;
import mayo.job.server.JobServerProperties;
import mayo.job.server.async.disruptor.assembly.JobDisruptorPublisher;
import mayo.job.server.async.disruptor.assembly.JobDisruptorSubscriber;
import mayo.job.server.async.disruptor.strategy.JobAllotStrategy;
import mayo.job.server.async.disruptor.strategy.JobPullStrategy;
import mayo.job.store.AsyncJobStorer;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * disruptor实现的job服务器(处理异步请求)
 */
@Component("asyncServer")
@Slf4j
public class JobDisruptorServer implements JobServer {

    @Autowired
    private JobServerProperties jobServerProperties;
    @Autowired
    private JobDisruptorPublisher jobDisruptorPublisher;
    @Autowired
    private AsyncJobStorer asyncJobStorer;
    @Autowired
    private JobAllotStrategy jobAllotStrategy;
    @Autowired
    private JobPullStrategy jobPullStrategy;

    private JobService jobService;
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
    public void startup() throws SchedulerException {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("async-subscriber").build();
        workThreadPool = Executors.newFixedThreadPool(jobServerProperties.getWorkCount(), threadFactory);
        JobDisruptorSubscriber[] jobDisruptorSubscribers = new JobDisruptorSubscriber[jobServerProperties.getWorkCount()];
        for (int i = 0 ; i < jobServerProperties.getWorkCount() ; i++) {
            jobDisruptorSubscribers[i] = new JobDisruptorSubscriber(jobService, asyncJobStorer);
        }
        workerPool = new WorkerPool<>(jobDisruptorPublisher.getRingBuffer(),
                jobDisruptorPublisher.getSubscribeBarrier(), new IgnoreExceptionHandler(), jobDisruptorSubscribers);
        Sequence[] sequences = workerPool.getWorkerSequences();
        jobDisruptorPublisher.getRingBuffer().addGatingSequences(sequences);
        workerPool.start(workThreadPool);

        jobAllotStrategy.setJobNameList(jobService.getJobNameList());
        jobPullStrategy.setJobNameList(jobService.getJobNameList());
        jobDisruptorPublisher.startup();
        log.info("the asyncJobServer is run.");
    }

    /**
     * 停止异步服务器
     */
    @Override
    public void shutdown() {
        jobDisruptorPublisher.shutdown();
        workerPool.halt();
        workThreadPool.shutdown();
    }
}

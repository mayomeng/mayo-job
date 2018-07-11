package mayo.job.client.impl;

import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.client.JobClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import mayo.job.parent.param.JobParam;
import mayo.job.client.netty.JobChannelPool;
import mayo.job.store.AsyncJobStorer;

import java.util.concurrent.locks.LockSupport;

/**
 * 任务客户端
 */
@Slf4j
public class JobClientImpl implements JobClient {

    public final static AttributeKey<JobClientImpl> JOB_CLIENT = AttributeKey.newInstance("JobClient");

    private JobChannelPool pool;
    @Setter
    private JobParam jobResult;
    private AsyncJobStorer asyncJobStorer;
    @Getter
    @Setter
    private Thread syncRequestThread;

    public JobClientImpl(JobChannelPool pool, AsyncJobStorer asyncJobStorer) {
        this.pool = pool;
        this.asyncJobStorer = asyncJobStorer;
    }

    @Override
    public JobParam syncRequest(JobParam jobParam) throws Exception {
        Channel channel = pool.getChannel();
        setSyncRequestThread(Thread.currentThread());
        channel.attr(JOB_CLIENT).set(this);
        ChannelFuture channelFuture = channel.writeAndFlush(jobParam).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    //log.info(future.cause().getMessage());
                }
            }
        });
        LockSupport.park();
        pool.release(channel);
        return jobResult;
    }


    @Override
    public long asynRequest(JobParam jobParam) {
        long jobId = asyncJobStorer.createJob(jobParam);
        return jobId;
    }

    @Override
    public JobParam queryResult(long jobId) {
            try {
                for (;!asyncJobStorer.isJobComplete(jobId);) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        JobParam jobResult = asyncJobStorer.getJobResult(jobId);
        if (jobResult.isSuccess()) { // 执行成功的时候，删除缓存结果
            asyncJobStorer.removeJob(jobResult.getJobId());
        }
        return jobResult;
    }
}

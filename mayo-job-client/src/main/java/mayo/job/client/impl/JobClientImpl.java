package mayo.job.client.impl;

import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import mayo.job.client.JobClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;
import mayo.job.client.netty.JobChannelPool;

/**
 * Marshalling协议任务客户端
 */
@Slf4j
public class JobClientImpl implements JobClient {

    public final static AttributeKey<JobClientImpl> JOB_CLIENT = AttributeKey.newInstance("JobClient");

    private JobChannelPool pool;

    private JobResult jobResult;

    public JobClientImpl(JobChannelPool pool) {
        this.pool = pool;
    }

    @Override
    public JobResult syncRequest(JobParam jobParam) {
        Channel channel = pool.getChannel();
        channel.attr(JOB_CLIENT).set(this);
        ChannelFuture channelFuture = channel.writeAndFlush(jobParam);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    //log.info(future.cause().getMessage());
                }
            }
        });
        for (;;) {
            if (jobResult != null) {
                log.debug("the client thread {}", Thread.currentThread().getName());
                break;
            }
        }
        pool.release(channel);
        return jobResult;
    }


    @Override
    public long asynRequest(JobParam jobParam) {
        Channel channel = pool.getChannel();
        channel.writeAndFlush(jobParam);
        pool.release(channel);
        return 0;
    }

    @Override
    public JobResult queryResult(long jobId) {
        return null;
    }

    public void setResult(JobResult jobResult) {
        this.jobResult = jobResult;
    }
}

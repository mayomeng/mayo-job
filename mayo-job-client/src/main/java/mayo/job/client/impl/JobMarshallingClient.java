package mayo.job.client.impl;

import mayo.job.client.JobClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Marshalling协议任务客户端
 */
@Component
public class JobMarshallingClient implements JobClient {

    @Autowired
    private JobMarshallingChannelPool pool;

    @Override
    public JobResult syncRequest(JobParam jobParam) {
        JobResult jobResult;
        Channel channel = pool.getChannel();
        ChannelFuture channelFuture = channel.writeAndFlush(jobParam);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {

            }
        });
        pool.release(channel);
        return null;
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
}

package mayo.job.client.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.result.JobResult;
import mayo.job.client.impl.JobClientImpl;
import org.springframework.stereotype.Component;

/**
 * Created by SKJ-05A14-0049 on 2018/3/7.
 */
@Sharable
@Slf4j
@Component
public class JobClientHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        JobClientImpl jobClient = ctx.channel().attr(JobClientImpl.JOB_CLIENT).get();
        log.debug("the channel thread {}", Thread.currentThread().getName());
        jobClient.setResult((JobResult)o);
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        // 异常的场合关闭channel
        ctx.channel().close();
    }
}

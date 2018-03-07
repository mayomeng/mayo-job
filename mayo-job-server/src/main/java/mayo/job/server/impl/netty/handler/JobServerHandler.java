package mayo.job.server.impl.netty.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import mayo.job.bean.result.JobResult;
import mayo.job.node.JobNodeContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 执行器handler
 */
@Sharable
@Slf4j
@Component
public class JobServerHandler extends SimpleChannelInboundHandler {

    @Autowired
    private JobNodeContainer jobNodeContainer;

    /**
     * 处理handler
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        JobResult result = jobNodeContainer.execute(o);
        ChannelFuture channelFuture = ctx.writeAndFlush(result);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("request is success {}", o);
                }
                ctx.channel().close(); // 处理完成后关闭channel
            }
        });
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

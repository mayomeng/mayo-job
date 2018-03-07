package mayo.job.client.impl;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
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
        ChannelFuture channelFuture = ctx.writeAndFlush(o);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("response is success {}", o);
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

package mayo.job.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理handler
 */
@Sharable
@Component
@Slf4j
public class JobHandler extends SimpleChannelInboundHandler {

    /**
     * 处理handler
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {


        // TODO 测试用
        String result = "{\n" +
                "    \"id\":\"1\",\n" +
                "    \"name\":\"test\"\n" +
                "}";
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpHeaders httpHeaders = response.headers();
        httpHeaders.add("param", "value");
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/json");
        //一定要设置长度，否则http客户端会一直等待（因为返回的信息长度客户端不知道）
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, result.length());
        ByteBuf responseContent = response.content();
        responseContent.writeBytes(result.getBytes("UTF-8"));
        ctx.writeAndFlush(response);
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常的场合关闭channel
        ctx.channel().close();
    }
}

package mayo.job.server.impl.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import mayo.job.bean.protocol.ProtocolConfiguration;
import mayo.job.server.impl.netty.handler.JobServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Marshalling协议通道初始类.
 */
@Component
public class JobChannelInitalizer extends ChannelInitializer<NioSocketChannel> {

    @Autowired
    private JobServerHandler jobServerHandler;

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(ProtocolConfiguration.getDecoder());
        ch.pipeline().addLast(ProtocolConfiguration.getEncoder());
        ch.pipeline().addLast(jobServerHandler);
    }
}

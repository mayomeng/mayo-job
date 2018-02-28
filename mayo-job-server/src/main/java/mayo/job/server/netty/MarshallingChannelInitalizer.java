package mayo.job.server.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Marshalling协议通道初始类.
 */
@Component
public class MarshallingChannelInitalizer extends ChannelInitializer<NioSocketChannel> {

    @Autowired
    private MarshallingDecoder marshallingDecoder;
    @Autowired
    private MarshallingEncoder marshallingEncoder;
    @Autowired
    private JobHandler jobHandler;

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(marshallingDecoder);
        ch.pipeline().addLast(marshallingEncoder);
        ch.pipeline().addLast(jobHandler);
    }
}

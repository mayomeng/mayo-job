package mayo.job.server.impl.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import lombok.Getter;
import lombok.Setter;
import mayo.job.server.impl.netty.handler.JobHandler;
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

    @Getter
    @Setter
    private JobHandler jobHandler;

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(marshallingDecoder);
        ch.pipeline().addLast(marshallingEncoder);
        ch.pipeline().addLast(jobHandler);
    }
}

package mayo.job.client.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Marshalling.
 */
@Component
public class MarshallingPoolHandler extends AbstractChannelPoolHandler {

    @Autowired
    private MarshallingDecoder marshallingDecoder;
    @Autowired
    private MarshallingEncoder marshallingEncoder;

    @Override
    public void channelCreated(Channel channel) throws Exception {
        ChannelPipeline p = channel.pipeline();
        p.addLast(marshallingEncoder);
        p.addLast(marshallingDecoder);
    }
}

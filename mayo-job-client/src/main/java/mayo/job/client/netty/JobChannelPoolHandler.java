package mayo.job.client.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import mayo.job.bean.protocol.ProtocolConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Marshalling.
 */
@Component
public class JobChannelPoolHandler extends AbstractChannelPoolHandler {

    @Autowired
    private JobClientHandler jobClientHandler;

    @Override
    public void channelCreated(Channel channel) throws Exception {
        ChannelPipeline p = channel.pipeline();
        p.addLast(ProtocolConfiguration.getEncoder());
        p.addLast(ProtocolConfiguration.getDecoder());
        p.addLast(jobClientHandler);
    }
}

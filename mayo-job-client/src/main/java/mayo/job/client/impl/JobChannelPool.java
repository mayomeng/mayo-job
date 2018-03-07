package mayo.job.client.impl;

import mayo.job.client.config.JobClientProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutionException;

/**
 * Marshalling协议的客户端连接池
 */
@Component
public class JobChannelPool {

    private ChannelPool channelPool;

    private NioEventLoopGroup group;

    @Autowired
    private JobClientProperties jobClientProperties;

    @Autowired
    private JobChannelPoolHandler jobChannelPoolHandler;

    /**
     * 初始化
     */
    public void init() {
        group = new NioEventLoopGroup(jobClientProperties.ClientThreadCount);
        Bootstrap bootstrap = new Bootstrap().channel(NioSocketChannel.class).group(group);
        bootstrap.remoteAddress(getJobServerAddress());
        channelPool = new FixedChannelPool(bootstrap, jobChannelPoolHandler, jobClientProperties.ClientCount);
    }

    /**
     * 取得调度器地址
     */
    private SocketAddress getJobServerAddress() {
        // TODO
        return InetSocketAddress.createUnresolved("192.168.4.195", 8091);
    }

    /**
     * 取得Channel
     */
    public Channel getChannel() {
        Channel channel;
        try {
            channel = channelPool.acquire().get();
        } catch (InterruptedException | ExecutionException e) {
            destroy();
            init();
            channel = getChannel();
        }
        return channel;
    }

    /**
     * 释放Channel
     */
    public void release(Channel channel) {
        channelPool.release(channel);
    }

    /**
     * 销毁连接池
     */
    public void destroy() {
        channelPool.close();
        group.shutdownGracefully();
    }
}

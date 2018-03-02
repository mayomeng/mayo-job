package mayo.job.server.impl.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import mayo.job.node.coordinate.JobCoordinate;
import mayo.job.server.JobServer;
import mayo.job.server.impl.netty.config.JobServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;

/**
 * netty实现的job服务器
 */
@Component
@Slf4j
public class JobNettyServer implements JobServer {

    @Autowired
    private JobServerProperties jobServerProperties;

    @Autowired
    private MarshallingChannelInitalizer channelInitializer;

    @Autowired
    private JobCoordinate jobCoordinate;

    private EventLoopGroup bossLoopGroup;
    private EventLoopGroup workLoogGroup;

    /**
     * 启动服务器
     */
    @Override
    public void startup() {
        jobCoordinate.election();
        startupService();
        jobCoordinate.monitor();
    }

    /**
     * 启动服务
     */
    private void startupService() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        bossLoopGroup = new NioEventLoopGroup(jobServerProperties.AccpectCount);
        workLoogGroup = new NioEventLoopGroup(jobServerProperties.WorkCount,
                new DefaultThreadFactory("work thread pool"), SelectorProvider.provider());
        serverBootstrap.group(bossLoopGroup , workLoogGroup);

        try {
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(channelInitializer);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128); // 服务端可连接队列大小
            serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // 使用对象池，重用缓冲区
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // 使用对象池，重用缓冲区
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(jobServerProperties.Host,
                    jobServerProperties.Port));
            channelFuture.channel().closeFuture().sync();
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}

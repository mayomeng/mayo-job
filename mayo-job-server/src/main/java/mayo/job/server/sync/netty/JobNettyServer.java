package mayo.job.server.sync.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.parent.protocol.ProtocolConfiguration;
import mayo.job.server.JobServer;
import mayo.job.parent.service.JobService;
import mayo.job.server.JobServerProperties;
import mayo.job.server.sync.netty.handler.JobServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;

/**
 * netty实现的job服务器(处理同步请求)
 */
@Component("syncServer")
@Slf4j
public class JobNettyServer implements JobServer {

    @Autowired
    private JobEnvironment jobEnvironment;

    @Autowired
    private JobServerProperties jobServerProperties;

    private JobService jobService;

    private EventLoopGroup bossLoopGroup;
    private EventLoopGroup workLoogGroup;

    @Override
    public void setService(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * 启动服务
     */
    @Override
    public void startup() {

        jobEnvironment.setHost(jobServerProperties.getHost());
        jobEnvironment.setPort(jobServerProperties.getPort());

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        bossLoopGroup = new NioEventLoopGroup(jobServerProperties.getAcceptCount());
        workLoogGroup = new NioEventLoopGroup(jobServerProperties.getWorkCount(),
                new DefaultThreadFactory("work thread pool"), SelectorProvider.provider());
        serverBootstrap.group(bossLoopGroup , workLoogGroup);

        try {
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(ProtocolConfiguration.getDecoder());
                    ch.pipeline().addLast(ProtocolConfiguration.getEncoder());
                    ch.pipeline().addLast(new JobServerHandler(jobService));
                }
            });
            serverBootstrap.option(ChannelOption.SO_BACKLOG, jobServerProperties.getBacklog()); // 服务端可连接队列大小
            serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // 使用对象池，重用缓冲区
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // 使用对象池，重用缓冲区
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(jobServerProperties.getHost(),
                    jobServerProperties.getPort()));
            log.info("the syncJobServer is run.");
            channelFuture.channel().closeFuture().sync();
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 关闭服务
     */
    @Override
    public void shutdown() {

        if (bossLoopGroup == null || workLoogGroup == null) {
            return;
        }

        Future bossCloseFuture = bossLoopGroup.shutdownGracefully();
        Future workCloseFuture = workLoogGroup.shutdownGracefully();

        bossCloseFuture.addListeners(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isDone()) {
                    log.info("the syncJobServer'accpect thread is shutdown.");
                }
            }
        });
        workCloseFuture.addListeners(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isDone()) {
                    log.info("the syncJobServer'work thread is shutdown.");
                }
            }
        });
    }
}

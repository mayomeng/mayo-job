package mayo.job.server.netty;

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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.parent.protocol.ProtocolConfiguration;
import mayo.job.server.JobServer;
import mayo.job.parent.service.JobService;
import mayo.job.server.netty.handler.JobServerHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;

/**
 * netty实现的job服务器
 */
@PropertySource("classpath:job.properties")
@ConfigurationProperties(prefix = "server")
@Component
@Slf4j
public class JobNettyServer implements JobServer {

    // 服务器配置
    @Setter
    private String host; // IP
    @Setter
    private int port; // 端口
    @Setter
    private int backlog; // 最大连接数(还需要修改服务器的somaxconn)
    @Setter
    private int accpectCount; // 响应线程数
    @Setter
    private int workCount; // 工作线程数
    @Setter
    private String protocol; // 协议

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
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        bossLoopGroup = new NioEventLoopGroup(accpectCount);
        workLoogGroup = new NioEventLoopGroup(workCount,
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
            serverBootstrap.option(ChannelOption.SO_BACKLOG, backlog); // 服务端可连接队列大小
            serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // 使用对象池，重用缓冲区
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // 使用对象池，重用缓冲区
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(host, port));
            channelFuture.channel().closeFuture().sync();
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}

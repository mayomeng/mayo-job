package mayo.job.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import mayo.job.server.JobServer;
import mayo.job.server.config.JobServerProperties;
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
    private HttpChannelInitalizer httpChannelInitalizer;

    private EventLoopGroup bossLoopGroup;
    private EventLoopGroup workLoogGroup;

    private Future bossCloseFuture;
    private Future workCloseFuture;

    /**
     * 启动服务器
     */
    @Override
    public void startup() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        bossLoopGroup = new NioEventLoopGroup(jobServerProperties.getAccpectThreadNum());
        workLoogGroup = new NioEventLoopGroup(jobServerProperties.getWorkThreadNum(),
                new DefaultThreadFactory("work thread pool"), SelectorProvider.provider());
        serverBootstrap.group(bossLoopGroup , workLoogGroup);

        try {
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(httpChannelInitalizer);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.bind(new InetSocketAddress(jobServerProperties.getHost(),
                    jobServerProperties.getPort()));
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 停止服务器
     */
    @Override
    public void shutdown() {
        bossCloseFuture = bossLoopGroup.shutdownGracefully();
        workCloseFuture = workLoogGroup.shutdownGracefully();
        bossCloseFuture.addListeners(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isDone()) {
                    log.info("监听线程组关闭");
                }
            }
        });
        workCloseFuture.addListeners(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isDone()) {
                    System.out.println("工作线程组关闭");
                }
            }
        });
    }
}

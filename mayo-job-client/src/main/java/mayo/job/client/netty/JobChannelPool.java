package mayo.job.client.netty;

import lombok.Setter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import mayo.job.config.zookeeper.CuratorOperation;
import mayo.job.config.zookeeper.ZookeeperProperties;
import mayo.job.parent.enums.NodeTypeEnum;
import mayo.job.parent.environment.JobEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

/**
 * Marshalling协议的客户端连接池
 */
@PropertySource("classpath:client.properties")
@ConfigurationProperties(prefix = "client")
@Component
@Slf4j
public class JobChannelPool {

    @Setter
    private int clientCount;
    @Setter
    private int clientThreadCount;

    private ChannelPool channelPool;

    private NioEventLoopGroup group;

    @Autowired
    private JobChannelPoolHandler jobChannelPoolHandler;
    @Autowired
    private CuratorOperation curatorOperation;
    @Autowired
    private ZookeeperProperties zookeeperProperties;

    /**
     * 初始化
     */
    public void init() throws Exception {
        SocketAddress socketAddress = getJobServerAddress();
        if (socketAddress != null) {
            group = new NioEventLoopGroup(clientThreadCount);
            Bootstrap bootstrap = new Bootstrap().channel(NioSocketChannel.class).group(group);
            bootstrap.remoteAddress(socketAddress);
            channelPool = new FixedChannelPool(bootstrap, jobChannelPoolHandler, clientCount);
        }
    }

    /**
     * 取得执行器地址
     */
    private SocketAddress getJobServerAddress() throws Exception {
        // 取得目前连接数最小的执行器地址
        List<Object> childDataList = curatorOperation.getChildData(zookeeperProperties.getExecuterPath(), JobEnvironment.class);
        int connectCount = 1000000;
        JobEnvironment jobEnvironment = null;
        for (Object childData : childDataList) {
            JobEnvironment tempEnviroment = (JobEnvironment)childData;
            if (NodeTypeEnum.TYPE_ASYNC.VALUE.equals(tempEnviroment.getNodeType())) { // 异步执行器以外的场合，重新获取
                continue;
            }
            if (connectCount > tempEnviroment.getConnectCount()) {
                connectCount = tempEnviroment.getConnectCount();
                jobEnvironment = tempEnviroment;
                jobEnvironment.setConnectCount(connectCount + 1);
            }
        }
        if (jobEnvironment != null) {
            curatorOperation.setEphemeralData(zookeeperProperties.getExecuterPath() + "/" + jobEnvironment.getNodeId(), jobEnvironment);
            return InetSocketAddress.createUnresolved(jobEnvironment.getHost(), jobEnvironment.getPort());
        }
        return null;
    }

    /**
     * 取得Channel
     */
    public Channel getChannel() throws Exception {
        if (channelPool == null) {
            log.error("the channelPool is empty!");
            return null;
        }

        Channel channel;
        try {
            channel = channelPool.acquire().get();
        } catch (Exception e) {
            // TODO 因为报【java.util.concurrent.RejectedExecutionException: event executor terminated】异常信息，待调查后放开注解 destroy();
            init();
            channel = getChannel();
        }
        return channel;
    }

    /**
     * 释放Channel
     */
    public void release(Channel channel) {
        if (channelPool == null) {
            log.error("the channelPool is empty!");
            return;
        }
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

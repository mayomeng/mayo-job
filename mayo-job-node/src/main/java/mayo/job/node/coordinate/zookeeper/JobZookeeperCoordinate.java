package mayo.job.node.coordinate.zookeeper;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mayo.job.config.zookeeper.CuratorOperation;
import mayo.job.config.zookeeper.ZookeeperProperties;
import mayo.job.node.coordinate.JobCoordinate;
import mayo.job.parent.enums.NodeRoleEnum;
import mayo.job.parent.environment.JobEnvironment;
import mayo.job.store.AsyncJobStorer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 任务协调器，负责调度器选举，监控.(zookeeper实现)
 */
@Component
@Slf4j
public class JobZookeeperCoordinate implements JobCoordinate {
    @Autowired
    private JobEnvironment jobEnvironment;
    @Autowired
    private CuratorFramework zookeeperClient;
    @Autowired
    private ZookeeperProperties zookeeperProperties;
    @Autowired
    private CuratorOperation curatorOperation;
    @Autowired
    private AsyncJobStorer asyncJobStorer;

    @Getter
    private String role;

    private LeaderLatch leaderLatch;

    /**
     * 初始化
     */
    @Override
    public void init() throws Exception {

    }

    /**
     * 取得执行器在zookeeper上的存储目录
     */
    private String getExecuterPath() {
        return zookeeperProperties.getExecuterPath() + "/" + jobEnvironment.getNodeId();
    }

    /**
     * 取得调度器在zookeeper上的存储目录
     */
    private String getDispatchPath() {
        return zookeeperProperties.getDispatchPath() + "/" + jobEnvironment.getNodeId();
    }

    /**
     * 进行调度器选举
     */
    @Override
    public void election() throws Exception {
        // 选举前先将节点角色设为中间角色
        role = NodeRoleEnum.ROLE_BETWEENNESS.VALUE;

        // 判断现在有没有调度器
        List<Object> disppatherList = curatorOperation.getChildData(zookeeperProperties.getDispatchPath(), JobEnvironment.class);
        if (!CollectionUtils.isEmpty(disppatherList)) { // 已经存在调度器的场合
            setExecuter();
            return;
        }

        // 选举
        leaderLatch = new LeaderLatch(zookeeperClient, zookeeperProperties.getLeaderPath(), jobEnvironment.getNodeId());
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                setDispatcher();
            }

            @Override
            public void notLeader() {
                setExecuter();
            }
        });
        leaderLatch.start();
        Thread.sleep(5000);
    }

    /**
     * 设置节点为协调器
     */
    private void setDispatcher() {
        role = NodeRoleEnum.ROLE_DISPATH.VALUE; // 选举成功的场合
        try {
            curatorOperation.setEphemeralData(getDispatchPath(), jobEnvironment);
            monitor();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置节点为执行器
     */
    private void setExecuter() {
        role = NodeRoleEnum.ROLE_EXECUTER.VALUE;// 选举失败的场合
        try {
            curatorOperation.setEphemeralData(getExecuterPath(), jobEnvironment);
            monitor();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 监控
     */
    @Override
    public void monitor() throws Exception {
        if (NodeRoleEnum.ROLE_DISPATH.VALUE.equals(getRole())) {
            monitorExecuter();
        } else if (NodeRoleEnum.ROLE_EXECUTER.VALUE.equals(getRole())) {
            monitorDispath();
        }
    }

    /**
     * 监控调度器
     */
    private void monitorDispath() throws Exception {
        List<Object> disppatherList = curatorOperation.getChildData(zookeeperProperties.getDispatchPath(), JobEnvironment.class);
        JobEnvironment dispatchJobEnvironment = (JobEnvironment)disppatherList.get(0);
        String dispatchNodeId = dispatchJobEnvironment.getNodeId();

        NodeCache cache = new NodeCache(zookeeperClient, zookeeperProperties.getDispatchPath() + "/" + dispatchNodeId);

        NodeCacheListener listener = () -> {
            ChildData data = cache.getCurrentData();
            if (data == null) { // 调度器宕机的场合，重新进行选举
                election();
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();
    }

    /**
     * 监控执行器
     */
    private void monitorExecuter() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(zookeeperClient, zookeeperProperties.getExecuterPath(), true);
        PathChildrenCacheListener listener = (client1, event) -> {
            if (null != event.getData() && PathChildrenCacheEvent.Type.CHILD_REMOVED.equals(event.getType())) {
                // 执行器被删除的场合，将任务分配给其他执行器
                JobEnvironment removedJobEnvironment = (JobEnvironment) JSON.parseObject(new String(event.getData().getData(), "UTF-8"), JobEnvironment.class);
                removedJobEnvironment.getJobList().forEach(jobName -> {
                    asyncJobStorer.reAllotJob(removedJobEnvironment.getNodeId(), jobName);
                });
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();
        // 执行器宕机的话，将执行器上没处理完的异步任务分配给其他执行器
    }

    /**
     * 销毁
     */
    @Override
    public void shutdown() {
        if (zookeeperClient != null && zookeeperClient.getState() != null) {
            CloseableUtils.closeQuietly(zookeeperClient);
        }
        if (leaderLatch != null && leaderLatch.getState() != null) {
            CloseableUtils.closeQuietly(leaderLatch);
        }
    }
}

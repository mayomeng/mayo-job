package mayo.job.config.zookeeper;

import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * zookeeper操作类.
 */
@Component
public class CuratorOperation {

    @Autowired
    private CuratorFramework zookeeperClient;

    /**
     * 更新临时节点数据
     */
    public void setEphemeralData(String executerPath, Object data) throws Exception {
        Stat stat = zookeeperClient.checkExists().forPath(executerPath);
        if (stat != null) {
            zookeeperClient.setData().forPath(executerPath, JSON.toJSONString(data).getBytes());
        } else {
            zookeeperClient.create().withMode(CreateMode.EPHEMERAL).forPath(executerPath, JSON.toJSONString(data).getBytes());
        }
    }

    /**
     * 更新永久节点数据
     */
    public void setPersistentData(String executerPath, Object data) throws Exception {
        Stat stat = zookeeperClient.checkExists().forPath(executerPath);
        if (stat != null) {
            zookeeperClient.setData().forPath(executerPath, JSON.toJSONString(data).getBytes());
        } else {
            zookeeperClient.create().forPath(executerPath, JSON.toJSONString(data).getBytes());
        }
    }
}

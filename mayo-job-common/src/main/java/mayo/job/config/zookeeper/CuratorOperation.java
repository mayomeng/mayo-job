package mayo.job.config.zookeeper;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.ArrayStack;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * zookeeper操作类.
 */
@Component
public class CuratorOperation {

    @Autowired
    private CuratorFramework zookeeperClient;

    /**
     * 关闭
     */
    public void close() {
        zookeeperClient.close();
    }

    /**
     * 更新临时节点数据
     */
    public void setEphemeralData(String executerPath, Object data) throws Exception {
        Stat stat = zookeeperClient.checkExists().forPath(executerPath);
        if (stat != null) {
            zookeeperClient.setData().forPath(executerPath, JSON.toJSONString(data).getBytes());
        } else {
            zookeeperClient.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL).forPath(executerPath, JSON.toJSONString(data).getBytes());
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
            zookeeperClient.create().creatingParentsIfNeeded()
                    .forPath(executerPath, JSON.toJSONString(data).getBytes());
        }
    }

    /**
     * 取得节点内容
     */
    public Object getData(String path, Class dataClass) throws Exception {
        Object result = null;
        Stat stat = zookeeperClient.checkExists().forPath(path);
        if (stat != null) {
            byte[] data = zookeeperClient.getData().forPath(path);
            if (data != null) {
                result = JSON.parseObject(new String(data, "UTF-8"), dataClass);
            }
        }
        return result;
    }

    /**
     * 取得子节点内容
     */
    public List<Object> getChildData(String path, Class dataClass) throws Exception {
        List<Object> resultList = new ArrayList<>();
        Stat stat = zookeeperClient.checkExists().forPath(path);
        if (stat != null) {
            List<String> childPathList = zookeeperClient.getChildren().forPath(path);
            if (!CollectionUtils.isEmpty(childPathList)) {
                for (String childPath : childPathList) {
                    Object result = getData(path + "/" + childPath, dataClass);
                    resultList.add(result);
                }
            }
        }
        return resultList;
    }
}

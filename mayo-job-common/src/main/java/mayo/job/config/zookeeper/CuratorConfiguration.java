package mayo.job.config.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Curator配置类
 */
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class CuratorConfiguration {

    @Autowired
    private ZookeeperProperties zkProperties;

    @Bean
    public CuratorFramework createCuratorClient() {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(
                zkProperties.getRetryBaseSleepTimeMs(), zkProperties.getRetryMaxRetries());
        CuratorFramework client = CuratorFrameworkFactory.builder()
/*                .aclProvider(getACLProvider())
                .authorization(zkProperties.getPlatformAclScheme(),
                        zkProperties.getAdminAuth().getBytes())*/
                .connectString(zkProperties.getConnections())
                .sessionTimeoutMs(zkProperties.getSessionTimeout())
                .connectionTimeoutMs(zkProperties.getConnectionTimeout())
                .retryPolicy(retryPolicy)
                .namespace(zkProperties.getNamespace())
                .build();
        client.start();
        return client;
    }

    // 设定命名空间(根节点)的操作权限
    private ACLProvider getACLProvider() {
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl;
            @Override
            public List<ACL> getDefaultAcl() {
                if(acl ==null){
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                    acl.clear();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id(zkProperties.getPlatformAclScheme(),
                            zkProperties.getAdminAuth())));
                    this.acl = acl;
                }
                return acl;
            }
            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };
        return aclProvider;
    }
}

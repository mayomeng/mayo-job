package mayo.job.store.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置类
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)// 开启属性注入,通过@autowired注入
public class RedisConfiguration {
    @Autowired
    private RedisProperties redisProperties;

    /**
     * 配置连接池
     */
    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        jedisPoolConfig.setTestOnBorrow(redisProperties.isTestOnBorrow());
        return jedisPoolConfig;
    }

    /**
     * 配置集群
     */
    @Bean
    public RedisClusterConfiguration getRedisClusterConfiguration() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        for (int i = 0; i < redisProperties.getRedisHosts().size() ; i++) {
            redisClusterConfiguration.addClusterNode(
                    new RedisNode(redisProperties.getRedisHosts().get(i), redisProperties.getPorts().get(i)));
        }
        redisClusterConfiguration.setMaxRedirects(redisProperties.getDefaultMaxRedirections());
        return redisClusterConfiguration;
    }

    /**
     * 配置连接工厂类
     */
    @Bean
    public JedisConnectionFactory getJedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(getRedisClusterConfiguration());
        jedisConnectionFactory.setPoolConfig(getJedisPoolConfig());
        jedisConnectionFactory.setTimeout(redisProperties.getDefaultTimeout());
        //jedisConnectionFactory.setPassword(redisProperties.getPassword());
        return jedisConnectionFactory;
    }

    /**
     * 配置RedisTemplate
     */
    @Bean
    public RedisTemplate getRedisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());
        return redisTemplate;
    }
}

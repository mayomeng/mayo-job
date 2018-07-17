package mayo.job.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类(使用Lettuce)
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)// 开启属性注入,通过@autowired注入
public class LettuceConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 连接池配置文件
     */
    @Bean
    public GenericObjectPoolConfig getPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        genericObjectPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        genericObjectPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
        //在borrow一个实例时，是否提前进行alidate操作；如果为true，则得到的实例均是可用的
        genericObjectPoolConfig.setTestOnBorrow(redisProperties.isTestOnBorrow());
        //调用returnObject方法时，是否进行有效检查
        //genericObjectPoolConfig.setTestOnReturn(isTestOnReturn);
        //在空闲时检查有效性, 默认false
        //genericObjectPoolConfig.setTestWhileIdle(isTestWhileIdle);
        //表示idle object evitor两次扫描之间要sleep的毫秒数；
        //genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        //表示一个对象至少停留在idle状态的最短时间，
        //然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
        //genericObjectPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        return genericObjectPoolConfig;
    }

    /**
     * lettuce 连接工厂配置
     */
    @Bean
    public LettuceConnectionFactory getLettuceConnectionFactory() {
        LettucePoolingClientConfiguration poolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(getPoolConfig())
                .build();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
                redisProperties.getHost(), redisProperties.getPort()
        );

        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration, poolingClientConfiguration);
        //校验连接是否有效
        factory.setValidateConnection(true);
        //选择数据库
        //factory.setDatabase(0);
        factory.afterPropertiesSet();
        return factory;
    }

    /**
     * 配置RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setConnectionFactory(getLettuceConnectionFactory());
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}

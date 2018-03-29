package mayo.job.parent.common;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对象映射配置类
 */
@Configuration
public class MapperConfiguration {
    @Bean
    public Mapper getMapper() {
        return new DozerBeanMapper();
    }
}

package mayo.job.protocol.config;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 协议配置类.
 */
@Configuration
public class ProtocolConfiguration {

    /**
     * 协议解码.
     */
    @Bean
    public MarshallingDecoder getMarshallingDecoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(
                marshallerFactory, configuration);
        int maxSize = 1024 << 2;
        MarshallingDecoder decoder = new MarshallingDecoder(provider, maxSize);
        return decoder;
    }

    /**
     * 协议解编码
     */
    @Bean
    public MarshallingEncoder getMarshallingEncoder() {
        final MarshallerFactory marshallerFactory = Marshalling
                .getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(
                marshallerFactory, configuration);
        MarshallingEncoder decoder = new MarshallingEncoder(provider);
        return decoder;
    }
}

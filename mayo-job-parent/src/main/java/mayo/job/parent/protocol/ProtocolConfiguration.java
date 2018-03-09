package mayo.job.parent.protocol;

import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * 协议配置类.
 */
public class ProtocolConfiguration {

    public static MarshallingDecoder getDecoder() {
        return getMarshallingDecoder();
    }

    public static MarshallingEncoder getEncoder() {
        return getMarshallingEncoder();
    }

    /**
     * Http解码器
     */
    private static HttpRequestDecoder getHttpRequestDecoder() {
        return new HttpRequestDecoder();
    }

    /**
     * Http编码器
     */
    private static HttpResponseEncoder getHttpResponseEncoder() {
        return new HttpResponseEncoder();
    }

    /**
     * Marshalling解码器.
     */
    private static MarshallingDecoder getMarshallingDecoder() {
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
     * Marshalling编码器.
     */
    private static MarshallingEncoder getMarshallingEncoder() {
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

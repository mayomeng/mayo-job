package mayo.job.parent.protocol;

import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import lombok.Getter;
import lombok.Setter;

/**
 * 解码器
 */
@Getter
@Setter
public class JobDecoder {
    private MarshallingDecoder marshallingDecoder;
    private HttpRequestDecoder httpRequestDecoder;
}

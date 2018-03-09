package mayo.job.parent.protocol;

import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import lombok.Getter;
import lombok.Setter;

/**
 * 编码器
 */
@Getter
@Setter
public class JobEncoder {
    private MarshallingEncoder marshallingEncoder;
    private HttpResponseEncoder httpResponseEncoder;
}

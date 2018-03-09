package mayo.job.parent.enums;

/**
 * 协议枚举类
 */
public enum ProtocolEnum {
    HTTP("Http"),
    MARSHALLING("Marshalling");

    ProtocolEnum(String value) {
        this.VALUE = value;
    }

    public String VALUE;
}

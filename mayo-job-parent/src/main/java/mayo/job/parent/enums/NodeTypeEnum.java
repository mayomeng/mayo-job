package mayo.job.parent.enums;

/**
 * 节点类型
 */
public enum NodeTypeEnum {
    TYPE_SYNC("sync"), // 执行同步任务的节点
    TYPE_ASYNC("async"), // 执行异步任务的节点
    TYPE_BOTH("both"); // 执行同步，异步任务的节点

    public String VALUE;

    NodeTypeEnum(String value) {
        this.VALUE = value;
    }
}

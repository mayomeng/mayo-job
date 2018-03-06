package mayo.job.bean.enums;

/**
 * 节点角色枚举.
 */
public enum NodeRoleEnum {
    ROLE_BETWEENNESS("betweenness"), // 中间角色（选举期间节点的状态，此状态下的节点不响应客户端调用）
    ROLE_DISPATH("dispatch"), // 调度器角色
    ROLE_EXECUTER("executer"); // 执行器角色

    public String VALUE;

    NodeRoleEnum(String value) {
        this.VALUE = value;
    }
}

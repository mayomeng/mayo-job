package mayo.job.bean.enumBean;

/**
 * 节点角色枚举.
 */
public enum JobRoleEnum {
    ROLE_DISPATH("dispatch"),
    ROLE_EXECUTER("executer");

    public String VALUE;

    JobRoleEnum(String value) {
        this.VALUE = value;
    }
}

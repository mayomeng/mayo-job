package mayo.job.bean.enums;

/**
 * 任务类型枚举类.
 */
public enum JobTypeEnum {
    GENERAL_JOB("general"), // 普通任务
    TIMING_JOB("timing"), // 定时任务
    LINKED_JOB("linked"), // 链式任务
    MULI_JOB("muli"); // 并发任务

    public String VALUE;

    JobTypeEnum(String value) {
        this.VALUE = value;
    }
}

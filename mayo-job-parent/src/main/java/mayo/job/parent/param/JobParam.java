package mayo.job.parent.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 普通任务参数类.
 */
@Getter
@Setter
@ToString
public class JobParam implements Serializable {
    protected String jobName; // 任务名称(根据此字段选择执行器)
    protected Date submitTime; // 任务提交时间
    protected Date createTime; // 任务创建时间
    protected Object params; // 任务参数
}

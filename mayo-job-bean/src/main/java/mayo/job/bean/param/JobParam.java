package mayo.job.bean.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.param.JobParam;

import java.io.Serializable;

/**
 * 普通任务参数类.
 */
@Getter
@Setter
@ToString
public class JobParam implements Serializable {
    protected String jobName; // 任务名称(根据此字段选择执行器)
    protected  boolean isAsyn = false; // 是否是异步任务
}

package mayo.job.bean.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.param.JobParam;

import java.io.Serializable;
import java.util.Date;

/**
 * 普通任务参数类.
 */
@Getter
@Setter
@ToString
public class JobParam implements Serializable {
    protected  boolean isAsyn; // 是否是异步任务
    protected String jobName; // 任务名称(根据此字段选择执行器)
    protected Date submitTime; // 任务提交时间
    protected Date createTime; // 任务创建时间
    protected Object params; // 任务参数
}

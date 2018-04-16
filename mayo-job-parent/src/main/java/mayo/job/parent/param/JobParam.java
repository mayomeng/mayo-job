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
    protected long jobId; // 任务ID
    protected String nodeId; // 任务执行节点ID
    protected String jobName; // 任务名称(根据此字段选择执行器)
    protected Date submitTime; // 任务提交时间
    protected Date createTime; // 任务创建时间
    protected Object params; // 任务参数
    protected Date endTime; // 任务结束时间
    protected boolean isSuccess; // 成功 or 失败
    protected Object result; // 任务执行结果
}

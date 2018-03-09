package mayo.job.parent.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务执行结果
 */
@Getter
@Setter
@ToString
public class JobResult implements Serializable {
    protected long jobId; // 任务ID
    protected String jobNodeId; // 任务执行节点ID
    protected Date endTime; // 任务结束时间
    protected boolean isSuccess; // 成功 or 失败
    protected Object result; // 任务执行结果
}

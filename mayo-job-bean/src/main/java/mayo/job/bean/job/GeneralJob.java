package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

import java.io.Serializable;
import java.util.Date;

/**
 * 普通任务.
 */
@Getter
@Setter
@ToString
public class GeneralJob implements Serializable {
    protected  boolean isAsyn; // 是否是异步任务
    protected long jobId; // 任务ID
    protected String jobNode; // 任务执行节点
    protected JobParam jobParam; // 任务参数
    protected Date submitTime; // 任务提交时间
    protected Date createTime; // 任务创建时间
    protected Date endTime; // 任务结束时间
    protected JobResult result; // 任务执行结果
}

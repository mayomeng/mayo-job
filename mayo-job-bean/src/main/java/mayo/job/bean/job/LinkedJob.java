package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

import java.util.Date;

/**
 * 链式任务
 */
@Getter
@Setter
@ToString
public class LinkedJob {
    private long jobId; // 任务ID
    private String jobNode; // 任务执行节点
    private JobParam jobParam; // 任务参数
    private Date createTime; // 任务创建时间
    private Date endTime; // 任务结束时间
    private JobResult result; // 任务执行结果
    private long nextJobId; // 下一个任务ID
    private int jobCount; // 任务个数
}

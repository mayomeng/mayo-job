package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

import java.util.Date;

/**
 * 并发任务(父任务)
 */
@Getter
@Setter
@ToString
public class MuliParentJob {
    private long jobId; // 任务ID
    private Date createTime; // 任务创建时间
    private Date endTime; // 任务结束时间
    private JobResult result; // 任务执行结果
    private int jobCount; // 任务个数
}

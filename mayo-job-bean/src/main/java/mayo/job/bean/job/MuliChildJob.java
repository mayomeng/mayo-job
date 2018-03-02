package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.result.JobResult;

/**
 * 并发任务(子任务)
 */
@Getter
@Setter
@ToString
public class MuliChildJob extends Job {
    protected JobResult result; // 任务执行结果
    protected long parentJobId; //父任务ID
}

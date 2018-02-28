package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

import java.util.Date;

/**
 * 并发任务(子任务)
 */
@Getter
@Setter
@ToString
public class MuliChildJob extends GeneralJob {
    protected JobResult result; // 任务执行结果
    protected long parentJobId; //父任务ID
}

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
public class LinkedJob extends GeneralJob {
    protected long nextJobId; // 下一个任务ID
    protected int jobCount; // 任务个数
}

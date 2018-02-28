package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.result.JobResult;

import java.util.Date;

/**
 * 并发任务(父任务)
 */
@Getter
@Setter
@ToString
public class MuliParentJob extends GeneralJob {
    protected int jobCount; // 任务个数
}

package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 并发任务(父任务)
 */
@Getter
@Setter
@ToString
public class MuliParentJob extends Job {
    protected int jobCount; // 任务个数
}

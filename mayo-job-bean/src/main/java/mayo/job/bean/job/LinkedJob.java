package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 链式任务
 */
@Getter
@Setter
@ToString
public class LinkedJob extends Job {
    protected long nextJobId; // 下一个任务ID
    protected int jobCount; // 任务个数
}

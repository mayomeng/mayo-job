package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 定时任务
 */
@Getter
@Setter
@ToString
public class TimingJob extends Job {
    protected String cron; // 任务执行周期
}

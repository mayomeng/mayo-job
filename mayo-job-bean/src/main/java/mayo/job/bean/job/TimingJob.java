package mayo.job.bean.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayo.job.bean.param.JobParam;
import mayo.job.bean.result.JobResult;

import java.util.Date;

/**
 * 定时任务
 */
@Getter
@Setter
@ToString
public class TimingJob extends GeneralJob {
    protected String cron; // 任务执行周期
}

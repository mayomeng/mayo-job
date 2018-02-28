package mayo.job.executer;

import mayo.job.bean.job.GeneralJob;

/**
 * 任务执行器基类.
 */
public interface JobExecuter {
    public GeneralJob execute(Object params);
}

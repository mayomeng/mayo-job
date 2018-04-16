package mayo.job.server.async.disruptor;

import com.lmax.disruptor.WorkHandler;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.service.JobService;
import mayo.job.store.AsyncJobStorer;

/**
 * 任务订阅者
 */
public class JobDisruptorSubscriber implements WorkHandler<JobParam> {

    private JobService jobService;
    private AsyncJobStorer asyncJobStorer;

    public JobDisruptorSubscriber(JobService jobService, AsyncJobStorer asyncJobStorer) {
        this.jobService = jobService;
        this.asyncJobStorer = asyncJobStorer;
    }

    /**
     * 处理任务
     */
    @Override
    public void onEvent(JobParam jobParam) throws Exception {
        JobParam jobResult = (JobParam)jobService.execute(jobParam);
        asyncJobStorer.setJobResult(jobResult);
        if (!jobResult.isSuccess()) { // 任务执行失败，将任务重新添加到未分配任务队列中
            asyncJobStorer.reCreateJob(jobParam);
        }
    }
}

package mayo.job.server.disruptor;

import com.lmax.disruptor.WorkHandler;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;
import mayo.job.parent.service.JobService;
import mayo.job.store.AsyncJobStorer;

/**
 * 任务订阅者
 */
public class JobSubscriber implements WorkHandler<JobParam> {

    private JobService jobService;
    private AsyncJobStorer asyncJobStorer;

    public JobSubscriber(JobService jobService, AsyncJobStorer asyncJobStorer) {
        this.jobService = jobService;
        this.asyncJobStorer = asyncJobStorer;
    }

    /**
     * 处理任务
     */
    @Override
    public void onEvent(JobParam jobParam) throws Exception {
        JobResult jobResult = (JobResult)jobService.execute(jobParam);
        asyncJobStorer.setJobResult(jobResult);
    }
}

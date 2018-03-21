package mayo.job.server.disruptor;

import com.lmax.disruptor.WorkHandler;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.service.JobService;

/**
 * 任务订阅者
 */
public class JobSubscriber implements WorkHandler<JobParam> {

    private JobService jobService;

    public JobSubscriber(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * 处理任务
     */
    @Override
    public void onEvent(JobParam jobParam) throws Exception {
        // TODO
    }
}

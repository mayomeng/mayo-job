package mayo.job.parent.server;

import mayo.job.parent.service.JobService;

/**
 * 任务服务器接口.
 */
public interface JobServer {
    void setService(JobService jobService);
    void startup();
}

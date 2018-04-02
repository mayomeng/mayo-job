package mayo.job.client.impl;

import mayo.job.client.JobClient;
import mayo.job.client.netty.JobChannelPool;
import mayo.job.store.AsyncJobStorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 客户端工厂类
 */
@Component
public class JobClientFactory {

    @Autowired
    private JobChannelPool pool;
    @Autowired
    private AsyncJobStorer asyncJobStorer;

    public JobClient getJobClient() {
        JobClient jobClient = new JobClientImpl(pool, asyncJobStorer);
        return jobClient;
    }
}

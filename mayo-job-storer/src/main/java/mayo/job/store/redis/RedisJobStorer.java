package mayo.job.store.redis;

import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;
import mayo.job.store.JobStorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 任务存储
 */
@Component
public class RedisJobStorer implements JobStorer {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public long createJob(JobParam jobParam) {
        return 0;
    }

    @Override
    public JobResult getJobResult(long jobId) {
        return null;
    }
}

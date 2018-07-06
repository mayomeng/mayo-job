package mayo.job.parent.job;

import com.google.common.util.concurrent.RateLimiter;
import mayo.job.parent.annotation.LimitMark;
import mayo.job.parent.control.LimitSwitch;
import mayo.job.parent.param.JobParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 任务类适配器.
 */
public abstract class JobAdapter implements Job {

    // 任务类型SET，一个任务可以有多种类型
    protected Set<String> jobTypeSet = new HashSet<>();

    @Autowired
    private LimitSwitch limitSwitch;

    // 限流器
    private RateLimiter rateLimiter;
    private int limitTimeOut;

    @PostConstruct
    protected void init() {
        LimitMark limitMark = this.getClass().getAnnotation(LimitMark.class);
        int limitRate = limitMark.limitRate();
        int limitTimeOut = limitMark.limitTimeOut();
        if (limitRate > 0) {
            rateLimiter = RateLimiter.create(limitRate);
            this.limitTimeOut = limitTimeOut;
        }
    }

    /**
     * 添加任务类型.
     */
    protected void addJobType(String jobType) {
        jobTypeSet.add(jobType);
    }

    /**
     * 设置任务类型.
     */
    protected abstract void setJobType();

    /**
     * 处理任务包装方法.
     */
    @Override
    public JobParam handle(JobParam jobParam) {
        if (limitSwitch.isSwitchOn() && rateLimiter != null) {
            if (!rateLimiter.tryAcquire(limitTimeOut, TimeUnit.MILLISECONDS)) {
                return null;
            }
        }
        return handleJob(jobParam);
    }

    @Override
    public Set<String> getJobType() {
        return jobTypeSet;
    }

    /**
     * 处理任务.
     */
    public abstract JobParam handleJob(JobParam jobParam);
}

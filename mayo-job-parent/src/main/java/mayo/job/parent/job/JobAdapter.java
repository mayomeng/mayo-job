package mayo.job.parent.job;

import lombok.Getter;
import mayo.job.parent.param.JobParam;
import mayo.job.parent.result.JobResult;

import java.util.HashSet;
import java.util.Set;

/**
 * 任务类适配器.
 */
public abstract class JobAdapter implements Job {

    protected Set<String> jobTypeSet = new HashSet<>();

    protected void addJobType(String jobType) {
        jobTypeSet.add(jobType);
    }

    protected abstract void setJobType();

    @Override
    public JobResult handle(JobParam jobParam) {
        return null;
    }
    @Override
    public Set<String> getJobType() {
        return jobTypeSet;
    }

    @Override
    public String getJobName() {
        return null;
    }
}

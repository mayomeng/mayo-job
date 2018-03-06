package mayo.job.node;

import lombok.Getter;
import mayo.job.node.dispatch.JobDispatch;
import mayo.job.node.executer.JobExecuter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务字典，存储任务类型和任务名称之间的映射关系.
 */
@Component
@Getter
public class JobDict {
    private Map<String, String> jobNameTypewMap = new HashMap<>(); // 任务名称-类型映射关系
    private Map<String, JobDispatch> jobDispatchMap = new HashMap<>(); // 任务类型-调度器映射关系
    private Map<String, JobExecuter> jobExecuterMap = new HashMap<>(); // 任务类型-执行器映射关系

    public void put(String jobType, JobDispatch jobDispatch) {
        jobDispatchMap.put(jobType, jobDispatch);
    }

    public void put(String jobType, JobExecuter jobExecuter) {
        if (!jobExecuterMap.containsKey(jobType)) {
            jobExecuterMap.put(jobType, jobExecuter);
        }
    }

    public void put(String jobName, String jobType) {
        jobNameTypewMap.put(jobName, jobType);
    }
  /**
     * 根据任务名称取得任务类型.
     */
    public String getJobType(String jobName) {
        return jobNameTypewMap.get(jobName);
    }
}

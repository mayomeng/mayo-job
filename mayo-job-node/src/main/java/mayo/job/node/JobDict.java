package mayo.job.node;

import lombok.Getter;
import mayo.job.node.dispatch.JobDispatch;
import mayo.job.node.executer.JobExecuter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 任务字典，存储任务类型和任务名称之间的映射关系.
 */
@Component
@Getter
public class JobDict {
    private Map<String, List<String>> jobTypeNameMap = new HashMap<>(); // 任务类型-名称映射关系
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

        if (jobTypeNameMap.containsKey(jobExecuter.getJobType())) {
            jobTypeNameMap.get(jobExecuter.getJobType()).add(jobExecuter.getJobName());
        } else {
            List<String> jobNameList = new LinkedList<>();
            jobNameList.add(jobExecuter.getJobName());
            jobTypeNameMap.put(jobExecuter.getJobType(), jobNameList);
        }

        if (!jobNameTypewMap.containsKey(jobExecuter.getJobName())) {
            jobNameTypewMap.put(jobExecuter.getJobName(), jobExecuter.getJobType());
        }
    }

    /**
     * 根据任务名称取得任务类型.
     */
    public String getJobType(String jobName) {
        return jobNameTypewMap.get(jobName);
    }

    /**
     * 根据任务类型取得任务名称
     */
    public List<String> getJobName(String jobType) {
        return jobTypeNameMap.get(jobType);
    }
}

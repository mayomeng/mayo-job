package mayo.job.store;

/**
 * 任务缓存KEY创造器.
 */
public class JobKeyCreator {

    /**
     * 用于生成自增长jobId的key
     */
    public static String getJobIdKey() {
        return "jobId";
    }

    /**
     * 取得待分配任务列表的key
     */
    public static String getUnallotJobListKey(String jobName) {
        return "unAllot:" + jobName;
    }

    /**
     * 取得待处理任务列表的key
     */
    public static String getPendingJobKey(String nodeId, String jobName) {
        return "pending:" + nodeId +  ":" + jobName;
    }

    /**
     * 取得处理中任务列表的key
     */
    public static String getHandlingJobList(String nodeId, String jobName) {
        return "handling:" + nodeId +  ":" + jobName;
    }

    /**
     * 取得完毕任务列表的key
     */
    public static String getCompleteJobList(String jobName) {
        return "complete:" + jobName;
    }

    /**
     * 取得任务MAp的key
     */
    public static String getJobKey(long jobId) {
        return "job:" + jobId;
    }
}

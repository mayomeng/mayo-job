package mayo.job.parent.annotation;

import java.lang.annotation.*;

/**
 * 任务注解.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobMark {
    String jobName();
}

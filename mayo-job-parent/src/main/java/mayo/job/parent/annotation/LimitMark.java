package mayo.job.parent.annotation;

import java.lang.annotation.*;

/**
 * 限流注解.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LimitMark {
    int limitRate() default 0; // 单位秒
    int limitTimeOut() default 0; // 单位毫秒
}

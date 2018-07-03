package mayo.job.parent.environment;

import mayo.job.parent.enums.NodeTypeEnum;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.InputStream;
import java.util.Properties;

/**
 * 异步执行器的判断条件类.
 */
public class AsyncCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        boolean isAsync = false;
        Resource resource = conditionContext.getResourceLoader().getResource("job.properties");
        try (InputStream in = resource.getInputStream()) {
            Properties p = new Properties();
            p.load(in);
            String nodeType = p.getProperty("node.nodeType");
            if (NodeTypeEnum.TYPE_ASYNC.VALUE.equals(nodeType) || NodeTypeEnum.TYPE_BOTH.VALUE.equals(nodeType)) {
                isAsync = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return isAsync;
    }
}

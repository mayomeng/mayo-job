package mayo.job.parent.control;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 限流开关.
 */
@Component
@Getter
@Setter
public class LimitSwitch {
    /**
     * 限流开关开启标志.(默认关闭)
     */
    private boolean isSwitchOn = false;
}

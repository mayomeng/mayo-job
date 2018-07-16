package mayo.job;

import lombok.extern.slf4j.Slf4j;
import mayo.job.node.JobNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 任务框架启动类.
 */
@EnableConfigurationProperties
@SpringBootApplication
@EnableScheduling
//@EnableAsync
@Slf4j
public class JobServerApplication implements CommandLineRunner {

    @Autowired
    private JobNode jobNode;

    @Override
    public void run(String... strings) throws Exception {
        jobNode.startup();
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(JobServerApplication.class, args);
        log.info("the mayo-job is run.");
    }
}

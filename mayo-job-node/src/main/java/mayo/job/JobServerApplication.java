package mayo.job;

import mayo.job.node.JobNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 任务框架启动类.
 */
@EnableConfigurationProperties
@SpringBootApplication
public class JobServerApplication implements CommandLineRunner {

    @Autowired
    private JobNode jobNode;

    @Override
    public void run(String... strings) throws Exception {
        jobNode.startup();
    }

    public static void main(String[] args) {
        SpringApplication.run(JobServerApplication.class, args);
    }
}

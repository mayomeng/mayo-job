package mayo.job;

import mayo.job.server.JobServer;
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
    private JobServer jobServer;

    @Override
    public void run(String... strings) throws Exception {
        jobServer.startup();
    }

    public static void main(String[] args) {
        SpringApplication.run(JobServerApplication.class, args);
    }
}

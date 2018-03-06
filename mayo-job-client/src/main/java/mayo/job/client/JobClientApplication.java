package mayo.job.client;

import mayo.job.client.impl.JobMarshallingChannelPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 任务框架客户端启动类.
 */
@EnableConfigurationProperties
@SpringBootApplication
public class JobClientApplication implements CommandLineRunner {

    @Autowired
    private JobMarshallingChannelPool pool;
    @Autowired
    private JobClient jobClient;

    @Override
    public void run(String... strings) throws Exception {
        pool.init();
    }
}

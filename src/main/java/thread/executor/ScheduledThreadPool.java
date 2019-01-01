package thread.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPool extends AbstractThreadPool{
    protected static final Logger logger = LoggerFactory.getLogger(ScheduledThreadPool.class);

    static ScheduledExecutorService schedule = null;
    protected static int queues = 0;

    public static void main(String[] args) {
        new ScheduledThreadPool().test();
    }

    public void test(){
        createScheduledThreadPool();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            threadSleep(i);
            schedule.schedule(new WorkThread(i,startTime),30,TimeUnit.SECONDS);
        }
    }



    public static void createScheduledThreadPool(){
        schedule = Executors.newScheduledThreadPool(2, new ThreadFactoryBuilder().setNameFormat("service-registry-%d").build());
    }


}

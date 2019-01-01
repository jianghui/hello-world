package thread.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class FixedThreadPool extends AbstractThreadPool{
    protected static final Logger logger = LoggerFactory.getLogger(FixedThreadPool.class);

    static ThreadPoolExecutor executor = null;
    protected static int queues = 0;

    public static void main(String[] args) {
        new FixedThreadPool().test();
    }

    public void test(){
        createFixedThreadPool();
        long startTime = System.currentTimeMillis();
        new MonitorThread(executor,startTime).start();
        for (int i = 0; i < 10; i++) {
            threadSleep(i);
            executor.submit(new WorkThread(i,startTime));
        }
    }



    public static void createFixedThreadPool(){
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3,new ThreadFactoryBuilder().setNameFormat("rpc-logger-%d").build());
    }


}

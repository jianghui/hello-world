package thread.executor;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class MonitorThread extends Thread{
    protected static final Logger logger = LoggerFactory.getLogger(MonitorThread.class);
    private ThreadPoolExecutor executor;
    private long startTime;

    public MonitorThread(ThreadPoolExecutor executor,long startTime){
        this.executor = executor;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        while (true){
            try {
                long time = (System.currentTimeMillis() - startTime)/1000;
                BlockingQueue<Runnable> queue = executor.getQueue();
                logger.info("第"+time+"秒 monitor——>   time:{},activeCount:{},poolSize:{},taskCount:{},queue:{}",
                        new Object[]{DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),executor.getActiveCount(),executor.getPoolSize(),executor.getTaskCount(),queue.size()});
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

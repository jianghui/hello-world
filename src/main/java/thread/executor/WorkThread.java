package thread.executor;

import com.alibaba.dubbo.common.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class WorkThread implements Runnable{
    protected static final Logger logger = LoggerFactory.getLogger(WorkThread.class);

    private volatile boolean running = true;
    private InvokerLog log;
    private long startTime;

    public WorkThread(InvokerLog log,long startTime) {
        this.log = log;
        this.startTime = startTime;
    }


    @Override
    public void run() {
        long time = (System.currentTimeMillis() - startTime)/1000;
        logger.info("worker thread run time:"+time+" worker thread[" + Thread.currentThread().getName() + "] started");
        /*try {
            logger.info("execute:" + JSON.json(log));
        }catch (IOException e){
            e.printStackTrace();
        }*/
        boolean runing = true;
        while (runing){
            long wait = 30;//所有线程、任务创建完后开始结束
            wait = 9;
            long endTime = (System.currentTimeMillis() - startTime)/1000 - (time + wait);
            if(endTime > 0){
                long exitTime = (System.currentTimeMillis() - startTime)/1000;
                logger.info("worker thread exit time:"+exitTime+" worker thread[" + Thread.currentThread().getName() + "] end");
                runing = false;
            }
        }
    }
}

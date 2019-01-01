package thread.executor;

import com.alibaba.dubbo.common.json.JSON;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class WorkThread implements Runnable{
    protected static final Logger logger = LoggerFactory.getLogger(WorkThread.class);

    private volatile boolean running = true;
    private long startTime;
    private int i;

    public WorkThread(int i,long startTime) {
        this.startTime = startTime;
        this.i = i;
    }


    @Override
    public void run() {
        long time = (System.currentTimeMillis() - startTime)/1000;
        logger.info("第"+i+"个消费者线程[" + Thread.currentThread().getName() + "]运行开始时间:"+DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        /*try {
            logger.info("execute:" + JSON.json(log));
        }catch (IOException e){
            e.printStackTrace();
        }*/
        boolean runing = true;
        while (runing){
            long wait = 30;//所有线程、任务创建完后开始结束
//            wait = 9;
            long endTime = (System.currentTimeMillis() - startTime)/1000 - (time + wait);
            if(endTime > 0){
                long exitTime = (System.currentTimeMillis() - startTime)/1000;
                logger.info("第"+i+"个消费者线程名称[" + Thread.currentThread().getName() + "]退出时间:"+DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")+",运行了:"+exitTime + "秒");
                runing = false;
            }
        }
    }
}

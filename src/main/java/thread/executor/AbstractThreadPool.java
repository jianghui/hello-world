package thread.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractThreadPool {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractThreadPool.class);

    public void threadSleep(int i){
        System.out.println();
        try {
            Thread.sleep(i*1*1000);
            logger.info("sleep time:"+i*1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RejectedExecutionHandler getHandler(){
        RejectedExecutionHandler handler = null;
//            handler = new ThreadPoolExecutor.AbortPolicy();//拒绝任务，抛出异常
        handler = new ThreadPoolExecutor.CallerRunsPolicy();//调用线程自己去处理
        //handler = new ThreadPoolExecutor.DiscardPolicy();//拒绝任务直接无声抛弃，没有异常信息
        //handler = new ThreadPoolExecutor.DiscardOldestPolicy();//对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列
//        handler = new MyHandler();//自定义拒绝策略
        return handler;
    }
}

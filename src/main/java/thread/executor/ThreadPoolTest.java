package thread.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThreadPoolTest {
    protected static final Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);

    static ThreadPoolExecutor executor = null;
    static ScheduledExecutorService schedule = null;
    protected static int queues = 0;

    public static void main(String[] args) {
        cacheThreadPool();
    }

    public static void fixedThreadPool(){
        createFixedThreadPool();
        long startTime = System.currentTimeMillis();
        monitor(startTime);
        execute(startTime);
    }

    public static void cacheThreadPool(){
        createCacheThreadPool();
        long startTime = System.currentTimeMillis();
        monitor(startTime);
        execute(startTime);
    }

    public static void scheduledThreadPool(){
        createScheduledThreadPool();
        long startTime = System.currentTimeMillis();
//        monitor(startTime);
        executeScheduled(startTime);
    }

    private static void execute(long startTime) {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int a = 2,b = finalI;
                    try {
                        Thread.sleep(finalI*3*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    InvokerLog log = new InvokerLog();
                    log.setMethodName("add");
                    log.setBeginTime(System.currentTimeMillis());
                    log.setParams(new Object[]{a,b});
                    int result = add(a,b);
                    log.setResult(result);
                    log.setEndTime(System.currentTimeMillis());
                    executor.submit(new WorkThread(log,startTime));
                }
            }).start();
        }
    }

    public static void createScheduledThreadPool(){
        schedule = Executors.newScheduledThreadPool(2, new ThreadFactoryBuilder().setNameFormat("service-registry-%d").build());
    }

    private static void executeScheduled(long startTime){
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int a = 2,b = finalI;
                    try {
                        Thread.sleep(finalI*3*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    InvokerLog log = new InvokerLog();
                    schedule.schedule(new WorkThread(log,startTime),30,TimeUnit.SECONDS);
                }
            }).start();
        }
    }

    public static void createCacheThreadPool(){
        if(executor == null){
            BlockingQueue<Runnable> queue;
            if(queues <= 0){
                queue =  new SynchronousQueue<>();//等待其他线程取走后才能继续添加，没有消费的话则一直创建线程到达maxinumPoolSize
            }
            else{
                queue = new LinkedBlockingQueue<>(queues);
            }
            RejectedExecutionHandler handler = null;
            //handler = new ThreadPoolExecutor.AbortPolicy();//拒绝任务，抛出异常
            handler = new ThreadPoolExecutor.CallerRunsPolicy();//调用线程自己去处理
            //handler = new ThreadPoolExecutor.DiscardPolicy();//拒绝任务直接无声抛弃，没有异常信息
            //handler = new ThreadPoolExecutor.DiscardOldestPolicy();//对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列
            executor = new ThreadPoolExecutor(3, 5,60L,
                    TimeUnit.SECONDS,queue,new ThreadFactoryBuilder().setNameFormat("rpc-service-%d").build(),handler);
        }
    }

    public static void createFixedThreadPool(){
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3,new ThreadFactoryBuilder().setNameFormat("rpc-logger-%d").build());
    }

    public static void monitor(long startTime){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        BlockingQueue<Runnable> queue = executor.getQueue();
                        logger.info("monitor——>   time:{}," +
                                        "poolSize:{}," +
//                                        "corePoolSize:{}," +
//                                        "maximumPoolSize:{}," +
                                        "queue:{}",
                                new Object[]{(System.currentTimeMillis() - startTime)/1000,
                                        executor.getPoolSize(),
//                                        executor.getCorePoolSize(),
//                                        executor.getMaximumPoolSize(),
                                        queue.size()});
                        Thread.sleep(1000);
                        System.out.println("");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static int add(int a,int b){
        return a + b;
    }
}

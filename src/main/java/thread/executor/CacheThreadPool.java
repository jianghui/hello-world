package thread.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class CacheThreadPool extends AbstractThreadPool{
    protected static final Logger logger = LoggerFactory.getLogger(CacheThreadPool.class);

    ThreadPoolExecutor executor = null;
    protected int queues = 0;

    public static void main(String[] args) {
        new CacheThreadPool().test();

    }

    public void test(){
        createCacheThreadPool();
        long startTime = System.currentTimeMillis();
        new MonitorThread(executor,startTime).start();
        for (int i = 0; i < 10; i++) {
            /**
             * 1.
             * 2.当线程池里面创建的线程达到maximumPoolSize时，不会再去继续创建线程，将执行相应的执行策略。
             * 如果选择的是调用自己的线程去处理的话就是选择main线程，如果一直没有消费者线程处理，则调用submit方法会一直阻塞
             */
            executor.execute(new WorkThread(i,startTime));
            threadSleep(i);
        }
    }



    public void createCacheThreadPool(){
        if(executor == null){
            BlockingQueue<Runnable> queue;
            Executors.newCachedThreadPool();
            if(queues <= 0){
                /**
                 * 1.等待其他线程取走后才能继续添加，没有消费的话则一直创建线程到达maxinumPoolSize
                 * 2.创建的线程达到maxinumPoolSize数量则会执行相应的策略
                 */
                queue =  new SynchronousQueue<>();
            }
            else{
                queue = new LinkedBlockingQueue<>(queues);
            }
            executor = new ThreadPoolExecutor(3, 5,60L,
                    TimeUnit.SECONDS,queue,new ThreadFactoryBuilder().setNameFormat("rpc-service-%d").build(),getHandler());
        }
    }

    public void test2(){
        /**
         * Executors.newCachedThreadPool()流程
         * 1.新添加的任务会一直等待消费者拿走
         * 2.由于corePoolSize为0，maximumPoolSize为最大值，如果任务一直没有被消费，则会创建新的线程
         * 3.步骤2中创建的线程将会去执行添加的任务
         * 4.任务执行完毕如果没有被回收(默认60S)将会去执行新的任务已达到线程复用
         */
        ExecutorService service = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("rpc-service-%d").build());
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("消费者线程" + Thread.currentThread().getName() + "开始运行,休眠" + finalI*2 + "秒");
                        Thread.sleep(finalI*2*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("消费者线程" + Thread.currentThread().getName() + "运行完毕");
                }
            });
            System.out.println("任务"+i+"添加完成");
        }
    }


}

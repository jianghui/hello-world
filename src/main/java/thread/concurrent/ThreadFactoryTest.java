package concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jhui on 2018/3/23.
 */
public class ThreadFactoryTest {

    public static void main(String[] args) {
        MyThreadFactory factory = MyThreadFactory.getInstance();
        for (int i = 0; i < 20; i++) {
            factory.newThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            }).start();
        }
    }

    static class MyThreadFactory implements ThreadFactory{
        private static MyThreadFactory factory = new MyThreadFactory();
        private AtomicInteger count = new AtomicInteger(0);

        private MyThreadFactory(){}

        public static MyThreadFactory getInstance(){
            return factory;
        }

        @Override
        public Thread newThread(Runnable r) {
            System.out.println("current thread:" + count.intValue());
            count.incrementAndGet();
            return new Thread(r);
        }
    }


}

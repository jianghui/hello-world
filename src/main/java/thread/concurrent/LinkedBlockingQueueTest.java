package thread.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jhui on 2018/3/21.
 */
public class LinkedBlockingQueueTest {
    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue();

    public static void main(String[] args) {
        final LinkedBlockingQueueTest test = new LinkedBlockingQueueTest();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                test.product();
            }
        });
        thread.start();
        Thread thread2 = new Thread(new Runnable(){
            @Override
            public void run() {
                test.consum();
            }
        });
        thread2.start();
    }

    public void product(){
        for (int i = 1; i < 10; i++) {
            try {
                Thread.sleep(1000 * i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("queue befor:" + queue.size());
                final int finalI = i;
                queue.put(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("current thread:" + Thread.currentThread().getName() + "-" + finalI);
                    }
                });
                System.out.println("queue after:" + queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("product end");
    }

    public void consum(){
        try {
            while (true){
                Thread.sleep(1000 * 5);
                Runnable thread = queue.take();
                thread.run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package thread.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    private ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws Exception {
        ReentrantLockTest test = new ReentrantLockTest();
        test.test3();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                test.tryLockTest();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test.tryLockTest2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    public void tryLockTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "将要去获取lock,当前时间:" + System.currentTimeMillis());
        try {
            if(lock.tryLock(3000, TimeUnit.MILLISECONDS)){
                System.out.println(Thread.currentThread().getName() + "获取锁,当前时间:" + System.currentTimeMillis());
            }
        }catch (InterruptedException e){
            System.out.println("1111111111111");
            e.printStackTrace();
        }
    }

    public void tryLockTest2() throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(7000);
            System.out.println("~~~~");
        }finally {
            lock.unlock();
        }
    }

    public void test3() throws Exception {
        final Lock lock = new ReentrantLock();
        lock.lock();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lockInterruptibly();
                } catch(InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted.");
                }
            }
        }, "child thread -1");

        t1.start();
        Thread.sleep(1000);

//        t1.interrupt();

        Thread.sleep(1000000);
    }
}

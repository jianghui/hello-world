package zookeeper.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DistributedLock implements Lock,Watcher {
    static int n = 500;

    private ZooKeeper zk = null;
    private String ROOT_LOCK = "/locks";
    private String lockName;
    private String waitLock;
    private String currentLock;
    private CountDownLatch countDownLatch;
    private int sessionTomeout = 3000;

    public DistributedLock(String url,String lockName){
        this.lockName = lockName;
        try {
            zk = new ZooKeeper(url,sessionTomeout,this);
            Stat stat = zk.exists(ROOT_LOCK,false);
            if(stat == null){
                zk.create(ROOT_LOCK,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void lock() {
        if(tryLock()){
            System.out.println(Thread.currentThread().getName() + " " + lockName + "获得了锁");
            return;
        } else {
            waitForLock();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.lock();
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) {
        if(tryLock()){
            return true;
        }
        return waitForLock();
    }

    @Override
    public boolean tryLock() {
        String splitStr = "_lock_";
        try {
            currentLock = zk.create(ROOT_LOCK + "/" + lockName + splitStr,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + " 创建节点 " + currentLock);
            List<String> childs = zk.getChildren(ROOT_LOCK,false);
            List<String> lockObjects = new ArrayList<String>();
            for(String node : childs){
                String _node = node.split(splitStr)[0];
                if(Objects.equals(_node,lockName)){
                    lockObjects.add(node);
                }
            }
            Collections.sort(lockObjects);
            System.out.println(Thread.currentThread().getName() + " 的锁是 " + currentLock);
            if(Objects.equals(currentLock,ROOT_LOCK + "/" + lockObjects.get(0))){
                return true;
            }
            String prevNode = currentLock.substring(currentLock.lastIndexOf(0) + 1);
            waitLock = lockObjects.get(Collections.binarySearch(lockObjects,prevNode) - 1);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean waitForLock(){
        try {
            Stat stat = zk.exists(ROOT_LOCK + "/" + waitLock,true);
            if(stat != null){
                System.out.println(Thread.currentThread().getName() + " 等待锁 " + ROOT_LOCK + "/" + waitLock);
                this.countDownLatch = new CountDownLatch(1);
                this.countDownLatch.await(1000,TimeUnit.MILLISECONDS);
                this.countDownLatch = null;
                System.out.println(Thread.currentThread().getName() + " 等到了锁");
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void unlock() {
        System.out.println("释放锁 " + currentLock);
        try {
            zk.delete(currentLock, -1);
            currentLock = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(this.countDownLatch != null){
            this.countDownLatch.countDown();
        }
    }

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            public void run() {
                DistributedLock lock = null;
                try {
                    lock = new DistributedLock("127.0.0.1:2181", "test");
                    lock.lock();
                    System.out.println(--n);
                    System.out.println(Thread.currentThread().getName() + "正在运行");
                } finally {
                    if (lock != null) {
                        lock.unlock();
                    }
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(runnable);
            t.start();
        }
    }
}

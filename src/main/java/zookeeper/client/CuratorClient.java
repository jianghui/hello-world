package zookeeper.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CuratorClient {
    private final static String SERVER = "127.0.0.1:2181";

    private CuratorFramework client;

    private Lock connectingLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        CuratorClient client = new CuratorClient();
        client.createClient();
        client.connect();
        client.watchPath("/node/cache");
        client.delete("/node");
        client.createPath("/node/cache","127.0.0.1".getBytes());
//        for (int i = 0; i < 10; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    client.createSeqPath("/node/rpc/server","127.0.0.1".getBytes());
//                }
//            }).start();
//        }
//        client.watchPath("/node/cache");
//        Thread.sleep(1000);
        System.out.println(client.isPathExist("/node/cache"));
        System.out.println(new String(client.getData("/node/cache")));
//        Thread.sleep(1000);
//        System.out.println(new String(client.getData("/node/cache")));
//        List<String> childs = client.getChildren("/node/rpc");
//        for(String s : childs){
//            System.out.println(s);
//        }
//        System.out.println(new String());
        Thread.sleep(300000);
    }


    public void delete(String path){
        try {
//            client.delete().forPath(path);//删除一个子节点
            client.delete().deletingChildrenIfNeeded().forPath(path);//删除节点并递归删除子节点
//            client.delete().withVersion(1).forPath(path);//指定版本进行删除
//            client.delete().guaranteed().forPath(path);//强制保证删除一个节点
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建临时节点
     * @param path
     * @param data
     */
    public void createTempPath(String path, byte[] data) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,data);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建临时顺序节点
     * @param path
     * @param data
     * @return
     */
    public String createSeqTempPath(String path, byte[] data) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建持久节点
     * @param path
     * @param data
     */
    public void createPath(String path, byte[] data) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建持久时序节点
     * @param path
     * @param data
     * @return
     */
    public String createSeqPath(String path, byte[] data) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isPathExist(String path){
        try {
            return client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void watchPath(String path) {
        try {
            client.getData().usingWatcher(new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("监听器watchEvent:" + com.alibaba.fastjson.JSON.toJSONString(watchedEvent));
                }
            }).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getChildren(String path){
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getData(String path){
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setData(String path, byte[] data){
        try {
            client.setData().forPath(path,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createClient(){
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 10);
        client = CuratorFrameworkFactory.builder()
                .sessionTimeoutMs(10000)
                .connectString(SERVER)
                .connectionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                switch (newState) {
                    case CONNECTED:
                        System.out.println("连接上了.....");
                        break;
                    case RECONNECTED:
                        System.out.println("RECONNECTED");
                        break;
                    case LOST:
                        System.out.println("LOST");
                        break;

                    case SUSPENDED:
                        System.out.println("SUSPENDED");
                        break;
                }
            }
        });
    }

    private long getZkSessionId(){
        try {
            return client.getZookeeperClient().getZooKeeper().getSessionId();
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void connect() {
        connectingLock.lock();
        try {
            if(client.getState() == CuratorFrameworkState.LATENT) {
                client.start();
            }
        }
        finally {
            connectingLock.unlock();
        }
        connectingAwait();

    }

    public void connectingAwait() {
        try {
            if(client != null){
                client.blockUntilConnected();
            }
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isConnected() {
        return client != null  && client.getZookeeperClient().isConnected();
    }

}

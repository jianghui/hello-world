package zookeeper.client;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkClient {
    private final static String SERVER = "127.0.0.1:2181";
    private final static int connectionTimeout = 10000;
    private ZooKeeper zk = null;
    CountDownLatch connectedSingal = new CountDownLatch(1);

    public static void main(String[] args) {
        ZkClient client = new ZkClient();
        client.createClient();
        try {
            client.opter();
            Thread.sleep(1000000);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.close();
    }

    public void opter() throws KeeperException, InterruptedException {
        // 创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
        zk.exists("/root", true);
        zk.create("/root", "mydata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("---------------------");

        // 在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
        zk.exists("/root/childone", true);
        zk.create("/root/childone", "childone".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("---------------------");

        // 删除/root/childone这个节点，第二个参数为版本，－1的话直接删除，无视版本

        zk.exists("/root/childone", true);
        zk.delete("/root/childone", -1);
        System.out.println("---------------------");

        zk.exists("/root", true);
        zk.delete("/root", -1);
        System.out.println("---------------------");
    }


    public void createClient(){
        try {
            this.zk = new ZooKeeper(SERVER, this.connectionTimeout, new DefaultZkWatcher( connectedSingal,this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        if (null != this.zk) {
            try {
                this.zk.close();
            } catch (InterruptedException var2) {
                ;
            }
            this.zk = null;
        }

    }


}

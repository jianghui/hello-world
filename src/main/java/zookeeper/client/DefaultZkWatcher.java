package zookeeper.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

public class DefaultZkWatcher implements Watcher {
    private CountDownLatch connectedSingal;
    private ZkClient client;

    public DefaultZkWatcher(CountDownLatch connectedSingal, ZkClient client) {
        this.connectedSingal = connectedSingal;
        this.client = client;
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("监听器watchEvent:" + com.alibaba.fastjson.JSON.toJSONString(event));
    }
}

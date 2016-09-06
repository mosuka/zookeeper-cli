package com.github.mosuka.zookeeper.cli.util;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperConnection {

  final CountDownLatch connSignal = new CountDownLatch(1);
  private ZooKeeper zookeeper;

  public ZooKeeperConnection() {
  }

  public ZooKeeper connect(String zookeeperServer, int sessionTimeout)
      throws IOException, InterruptedException {
    zookeeper = new ZooKeeper(zookeeperServer, sessionTimeout, new Watcher() {
      public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
          connSignal.countDown();
        }
      }
    });
    connSignal.await();

    return zookeeper;
  }

  public void close() throws InterruptedException {
    if (zookeeper != null) {
      zookeeper.close();
    }
  }
}

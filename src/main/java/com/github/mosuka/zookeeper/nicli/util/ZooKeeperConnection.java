/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mosuka.zookeeper.nicli.util;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperConnection {
    private static final String DEFAULT_SERVER = "localhost:2181";
    private static final int DEFAULT_SESSION_TIMEOUT = 3000;

    final CountDownLatch connSignal = new CountDownLatch(1);
    private ZooKeeper zookeeper;

    public ZooKeeperConnection() throws IOException, InterruptedException {
        this(DEFAULT_SERVER, DEFAULT_SESSION_TIMEOUT);
    }

    public ZooKeeperConnection(String zookeeperServer, int sessionTimeout) throws IOException, InterruptedException {
        connect(zookeeperServer, sessionTimeout);
    }

    public void connect(String zookeeperServer, int sessionTimeout) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(zookeeperServer, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                if (event.getState() == KeeperState.SyncConnected) {
                    connSignal.countDown();
                }
            }
        });
        connSignal.await();
    }

    public ZooKeeper getZooKeeper() {
        return zookeeper;
    }

    public void close() throws InterruptedException {
        if (zookeeper != null) {
            zookeeper.close();
        }
    }
}

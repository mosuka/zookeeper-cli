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

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZooKeeperConnectionTest extends ZooKeeperTestBase {
    public ZooKeeperConnection zkConnection = null;

    @Before
    public void before() throws Exception {
        super.before();
        zkConnection = new ZooKeeperConnection(server, timeout);
    }

    @After
    public void after() throws Exception {
        super.after();
        zkConnection.close();
    }

    @Test
    public void testGetZooKeeper() {
        ZooKeeper zk = zkConnection.getZooKeeper();

        String name = zk.getClass().getName();
        assertEquals("org.apache.zookeeper.ZooKeeper", name);

        boolean alive = zk.getState().isAlive();
        assertEquals(true, alive);
    }

    @Test
    public void testClose() {
        ZooKeeper zk = zkConnection.getZooKeeper();

        try {
            zk.close();
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

        boolean alive = zk.getState().isAlive();
        assertEquals(false, alive);
    }
}

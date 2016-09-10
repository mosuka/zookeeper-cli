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
package com.github.mosuka.zookeeper.nicli.command;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Quotas;
import org.apache.zookeeper.StatsTrack;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.github.mosuka.zookeeper.nicli.util.QuotaUtil;

public class DelQuotaCommand extends Command {
    public static final String DEFAULT_PATH = "/";
    public static final boolean DEFAULT_BYTES = false;
    public static final boolean DEFAULT_NUM_NODES = false;

    public DelQuotaCommand() {
        this("setquota");
    }

    public DelQuotaCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        String path = parameters.containsKey("path") ? (String) parameters.get("path") : DEFAULT_PATH;
        boolean bytes = parameters.containsKey("bytes") ? (Boolean) parameters.get("bytes") : DEFAULT_BYTES;
        boolean numNodes = parameters.containsKey("num_nodes") ? (Boolean) parameters.get("num_nodes")
                : DEFAULT_NUM_NODES;

        ZooKeeper zk = getZookeeperConnection().getZooKeeper();

        try {
            String parentPath = Quotas.quotaZookeeper + path;
            String quotaPath = Quotas.quotaZookeeper + path + "/" + Quotas.limitNode;

            if (zk.exists(quotaPath, false) == null) {
                setStatus(Command.STATUS_ERROR);
                setMessage("quota for " + path + " does not exist.");
                return;
            }

            byte[] data = zk.getData(quotaPath, false, new Stat());
            StatsTrack strack = new StatsTrack(new String(data));

            if (bytes && !numNodes) {
                strack.setBytes(-1L);
                zk.setData(quotaPath, strack.toString().getBytes(), -1);
            } else if (!bytes && numNodes) {
                strack.setCount(-1);
                zk.setData(quotaPath, strack.toString().getBytes(), -1);
            } else if (bytes && numNodes) {
                // delete till you can find a node with more than
                // one child
                List<String> children = zk.getChildren(parentPath, false);
                /// delete the direct children first
                for (String child : children) {
                    zk.delete(parentPath + "/" + child, -1);
                }
                // cut the tree till their is more than one child
                QuotaUtil.trimProcQuotas(zk, parentPath);
            } else {
                setStatus(Command.STATUS_ERROR);
                setMessage("too few arguments");
                return;
            }

            setStatus(Command.STATUS_SUCCESS);
            setMessage(Command.SUCCESS_MESSAGE);
        } catch (KeeperException | InterruptedException | IOException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        }
    }
}

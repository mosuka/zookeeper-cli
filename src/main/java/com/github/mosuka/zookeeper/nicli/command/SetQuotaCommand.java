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
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import com.github.mosuka.zookeeper.nicli.util.QuotaUtil;

public class SetQuotaCommand extends Command {
    public static final String DEFAULT_PATH = "/";
    public static final long DEFAULT_BYTES = -1L;
    public static final int DEFAULT_NUM_NODES = -1;

    public SetQuotaCommand() {
        this("setquota");
    }

    public SetQuotaCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        String path = parameters.containsKey("path") ? (String) parameters.get("path") : DEFAULT_PATH;
        long bytes = parameters.containsKey("bytes") ? (Long) parameters.get("bytes") : DEFAULT_BYTES;
        int numNodes = parameters.containsKey("num_nodes") ? (Integer) parameters.get("num_nodes") : DEFAULT_NUM_NODES;

        ZooKeeper zk = getZookeeperConnection().getZooKeeper();

        try {
            if (parameters.containsKey("bytes")) {
                QuotaUtil.createQuota(zk, path, bytes, -1);
            } else if (parameters.containsKey("num_nodes")) {
                QuotaUtil.createQuota(zk, path, -1L, numNodes);
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

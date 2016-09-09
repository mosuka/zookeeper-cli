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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.github.mosuka.zookeeper.nicli.util.ACLUtil;

public class CreateCommand extends Command {
    public static final String DEFAULT_PATH = "/";
    public static final String DEFAULT_DATA = "";
    public static final String DEFAULT_ACL = "";
    public static final boolean DEFAULT_EPHEMERAL = false;
    public static final boolean DEFAULT_SEQUENTIAL = false;
    public static final boolean DEFAULT_WATCH = false;

    public CreateCommand() {
        this("create");
    }

    public CreateCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        String path = parameters.containsKey("path") ? (String) parameters.get("path") : DEFAULT_PATH;
        String data = parameters.containsKey("data") ? (String) parameters.get("data") : DEFAULT_DATA;
        String acl = parameters.containsKey("acl") ? (String) parameters.get("acl") : DEFAULT_ACL;
        boolean ephemeral = parameters.containsKey("ephemeral") ? (Boolean) parameters.get("ephemeral")
                : DEFAULT_EPHEMERAL;
        boolean sequential = parameters.containsKey("sequential") ? (Boolean) parameters.get("sequential")
                : DEFAULT_SEQUENTIAL;
        boolean watch = parameters.containsKey("watch") ? (Boolean) parameters.get("watch") : DEFAULT_WATCH;

        ZooKeeper zk = getZookeeperConnection().getZooKeeper();

        try {
            byte[] byteData = data.getBytes();

            List<ACL> aclObj = Ids.OPEN_ACL_UNSAFE;
            if (acl.length() > 0) {
                aclObj = ACLUtil.parseACLs(acl);
            }

            CreateMode createMode = CreateMode.PERSISTENT;
            if (ephemeral && sequential) {
                createMode = CreateMode.EPHEMERAL_SEQUENTIAL;
            } else if (ephemeral) {
                createMode = CreateMode.EPHEMERAL;
            } else if (sequential) {
                createMode = CreateMode.PERSISTENT_SEQUENTIAL;
            }

            String newPath = null;
            List<String> paths = Arrays.asList(path.split("/"));
            StringBuilder sbPath = new StringBuilder();
            for (Iterator<String> i = paths.iterator(); i.hasNext();) {
                String p = i.next();
                if (p.length() == 0) {
                    continue;
                }
                sbPath.append("/" + p);

                Stat stat = zk.exists(sbPath.toString(), watch);
                if (stat != null) {
                    // already exist
                    if (i.hasNext()) {
                        // skip this node
                        continue;
                    } else {
                        // throw exception
                    }
                } else {
                    // znode does not exist
                    if (i.hasNext()) {
                        // sub node created by empty data
                        newPath = zk.create(sbPath.toString(), "".getBytes(), aclObj, createMode);
                    } else {
                        // create new node by data
                        newPath = zk.create(sbPath.toString(), byteData, aclObj, createMode);
                    }
                }
            }

            addResponse("new_path", newPath);

            setStatus(Command.STATUS_SUCCESS);
            setMessage(Command.SUCCESS_MESSAGE);
        } catch (KeeperException | InterruptedException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        }
    }
}

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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.github.mosuka.zookeeper.nicli.util.ACLUtil;
import com.github.mosuka.zookeeper.nicli.util.StatUtil;

public class SetAclCommand extends Command {
    public static final String DEFAULT_PATH = "/";
    public static final String DEFAULT_ACL = "";
    public static final int DEFAULT_VERSION = -1;
    public static final boolean DEFAULT_WITH_STAT = false;

    public SetAclCommand() {
        this("setacl");
    }

    public SetAclCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        Map<String, Object> statMap = new LinkedHashMap<String, Object>();

        String path = parameters.containsKey("path") ? (String) parameters.get("path") : DEFAULT_PATH;
        String acl = parameters.containsKey("acl") ? (String) parameters.get("acl") : DEFAULT_ACL;
        int version = parameters.containsKey("version") ? (Integer) parameters.get("version") : DEFAULT_VERSION;
        boolean withStat = parameters.containsKey("with_stat") ? (Boolean) parameters.get("with_stat")
                : DEFAULT_WITH_STAT;

        ZooKeeper zk = getZookeeperConnection().getZooKeeper();

        try {
            List<ACL> aclObj = Ids.OPEN_ACL_UNSAFE;
            if (acl.length() > 0) {
                aclObj = ACLUtil.parseACLs(acl);
            }

            Stat stat = zk.setACL(path, aclObj, version);
            statMap = StatUtil.stat2Map(stat);

            if (!statMap.isEmpty() && withStat) {
                putResponse("stat", statMap);
            }

            setStatus(Command.STATUS_SUCCESS);
            setMessage(Command.SUCCESS_MESSAGE);
        } catch (KeeperException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        } catch (InterruptedException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        }
    }
}

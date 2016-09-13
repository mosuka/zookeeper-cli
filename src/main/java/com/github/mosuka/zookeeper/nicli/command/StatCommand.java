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
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.github.mosuka.zookeeper.nicli.util.StatUtil;

public class StatCommand extends Command {
    public static final String DEFAULT_PATH = "/";
    public static final boolean DEFAULT_WATCH = false;

    public StatCommand() {
        this("stat");
    }

    public StatCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        Map<String, Object> statMap = new LinkedHashMap<String, Object>();

        String path = parameters.containsKey("path") ? (String) parameters.get("path") : DEFAULT_PATH;
        boolean watch = parameters.containsKey("watch") ? (Boolean) parameters.get("watch") : DEFAULT_WATCH;

        ZooKeeper zk = getZookeeperConnection().getZooKeeper();

        try {
            Stat stat = zk.exists(path, watch);
            if (stat == null) {
                throw KeeperException.NoNodeException.create(Code.NONODE, path);
            }
            statMap = StatUtil.stat2Map(stat);

            if (!statMap.isEmpty()) {
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

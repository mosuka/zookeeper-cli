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
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.github.mosuka.zookeeper.nicli.util.StatUtil;

public class LsCommand extends Command {
    public static final boolean DEFAULT_WATCH = false;
    public static final boolean DEFAULT_WITH_STAT = false;

    public LsCommand() {
        this("ls");
    }

    public LsCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        Map<String, Object> statMap = new LinkedHashMap<String, Object>();

        try {
            String path = (String) parameters.get("path");
            boolean watch = parameters.containsKey("watch") ? (Boolean) parameters.get("watch") : DEFAULT_WATCH;
            boolean withStat = parameters.containsKey("with_stat") ? (Boolean) parameters.get("with_stat")
                    : DEFAULT_WITH_STAT;

            ZooKeeper zk = getZookeeperConnection().getZooKeeper();

            Stat stat = new Stat();
            List<String> children = null;
            if (withStat) {
                children = zk.getChildren(path, watch, stat);
            } else {
                children = zk.getChildren(path, watch);
            }

            putResponse("children", children);
            if (stat != null && withStat) {
                putResponse("stat", StatUtil.stat2Map(stat));
            }

            setStatus(Command.STATUS_SUCCESS);
            setMessage(Command.SUCCESS_MESSAGE);
        } catch (KeeperException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        } catch (InterruptedException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        } catch (ClassCastException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        } catch (NullPointerException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        }
    }
}

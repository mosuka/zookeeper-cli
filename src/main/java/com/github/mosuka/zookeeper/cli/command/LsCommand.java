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
package com.github.mosuka.zookeeper.cli.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.github.mosuka.zookeeper.cli.util.StatUtil;

public class LsCommand extends Command {
  public LsCommand() {
    this("ls");
  }

  public LsCommand(String name) {
    super(name);
  }

  @Override
  public void run(Map<String, Object> parameters) {
    List<String> children = new ArrayList<String>();
    Map<String, Object> statMap = new LinkedHashMap<String, Object>();

    String path = (String) parameters.get("path");
    boolean watch = (Boolean) parameters.get("watch");
    boolean withStat = (Boolean) parameters.get("with_stat");

    try {
      ZooKeeper zookeeper = getZookeeperConnection().getZooKeeper();

      if (withStat) {
        Stat stat = new Stat();
        children = zookeeper.getChildren(path, watch, stat);
        statMap = StatUtil.stat2Map(stat);
      } else {
        children = zookeeper.getChildren(path, watch);
      }

      addResponse("children", children);
      if (!statMap.isEmpty()) {
        addResponse("stat", statMap);
      }

      setExitCode(Command.SUCCESS_EXIT_CODE);
      setMessage(Command.SUCCESS_MESSAGE);
    } catch (KeeperException | InterruptedException e) {
      setExitCode(Command.ERROR_EXIT_CODE);
      setMessage(e.getMessage());
    }
  }

}

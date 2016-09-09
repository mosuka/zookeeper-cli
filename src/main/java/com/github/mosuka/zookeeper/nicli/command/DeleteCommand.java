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

import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZKUtil;
import org.apache.zookeeper.ZooKeeper;

public class DeleteCommand extends Command {
    public static final String DEFAULT_PATH = "/";
    public static final int DEFAULT_VERSION = -1;
    public static final boolean DEFAULT_RECURSIVE = false;

    public DeleteCommand() {
        this("delete");
    }

    public DeleteCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        String path = parameters.containsKey("path") ? (String) parameters.get("path") : DEFAULT_PATH;
        int version = parameters.containsKey("version") ? (Integer) parameters.get("version") : DEFAULT_VERSION;
        boolean recursive = parameters.containsKey("recursive") ? (Boolean) parameters.get("recursive")
                : DEFAULT_RECURSIVE;

        try {
            ZooKeeper zk = getZookeeperConnection().getZooKeeper();

            if (recursive) {
                ZKUtil.deleteRecursive(zk, path);
            } else {
                zk.delete(path, version);
            }

            setStatus(Command.STATUS_SUCCESS);
            setMessage(Command.SUCCESS_MESSAGE);
        } catch (KeeperException | InterruptedException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        }
    }
}

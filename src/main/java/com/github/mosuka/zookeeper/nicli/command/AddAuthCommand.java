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

import org.apache.zookeeper.ZooKeeper;

public class AddAuthCommand extends Command {
    public static final String DEFAULT_SCHEME = "";
    public static final String DEFAULT_AUTH = "";

    public AddAuthCommand() {
        this("addauth");
    }

    public AddAuthCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        String scheme = parameters.containsKey("scheme") ? (String) parameters.get("scheme") : DEFAULT_SCHEME;
        String auth = parameters.containsKey("auth") ? (String) parameters.get("auth") : DEFAULT_AUTH;

        ZooKeeper zk = getZookeeperConnection().getZooKeeper();

        byte[] authByte = auth.getBytes();

        zk.addAuthInfo(scheme, authByte);

        setStatus(Command.STATUS_SUCCESS);
        setMessage(Command.SUCCESS_MESSAGE);
    }
}

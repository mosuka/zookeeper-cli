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
import org.apache.zookeeper.Quotas;
import org.apache.zookeeper.StatsTrack;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ListQuotaCommand extends Command {
    public static final String DEFAULT_PATH = "/";

    public ListQuotaCommand() {
        this("listquota");
    }

    public ListQuotaCommand(String name) {
        super(name);
    }

    @Override
    public void run(Map<String, Object> parameters) {
        String path = parameters.containsKey("path") ? (String) parameters.get("path") : DEFAULT_PATH;
        String quotaPath = Quotas.quotaZookeeper + path + "/" + Quotas.limitNode;
        String statPath = Quotas.quotaZookeeper + path + "/" + Quotas.statNode;

        ZooKeeper zk = getZookeeperConnection().getZooKeeper();

        try {

            Stat quotaStat = new Stat();
            byte[] quotaData = zk.getData(quotaPath, false, quotaStat);
            StatsTrack quotaStatsTrack = new StatsTrack(new String(quotaData));

            Map<String, Object> quotaMap = new LinkedHashMap<String, Object>();
            quotaMap.put("path", quotaStatsTrack);
            quotaMap.put("count", quotaStatsTrack.getCount());
            quotaMap.put("bytes", quotaStatsTrack.getBytes());

            addResponse("quota", quotaMap);

            Stat statStat = new Stat();
            byte[] statData = zk.getData(statPath, false, statStat);
            StatsTrack statStatsTrack = new StatsTrack(new String(statData));
            System.out.println("Output stat for " + path + " " + statStatsTrack.toString());

            Map<String, Object> statMap = new LinkedHashMap<String, Object>();
            statMap.put("path", statPath);
            statMap.put("count", statStatsTrack.getCount());
            statMap.put("bytes", statStatsTrack.getBytes());

            setStatus(Command.STATUS_SUCCESS);
            setMessage(Command.SUCCESS_MESSAGE);
        } catch (KeeperException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage("quota for " + path + " does not exist.");
        } catch (InterruptedException e) {
            setStatus(Command.STATUS_ERROR);
            setMessage(e.getMessage());
        }
    }
}

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
package com.github.mosuka.zookeeper.cli.util;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.zookeeper.data.Stat;

public class StatUtil {

  public static Map<String, Object> stat2Map(Stat stat) {
    Map<String, Object> statMap = new LinkedHashMap<String, Object>();

    statMap.put("cZxid", "0x" + Long.toHexString(stat.getCzxid()));
    statMap.put("ctime", new Date(stat.getCtime()).toString());
    statMap.put("mZxid", "0x" + Long.toHexString(stat.getMzxid()));
    statMap.put("mtime", new Date(stat.getMtime()).toString());
    statMap.put("pZxid", "0x" + Long.toHexString(stat.getPzxid()));
    statMap.put("cversion", stat.getCversion());
    statMap.put("dataVersion", stat.getVersion());
    statMap.put("aclVersion", stat.getAversion());
    statMap.put("ephemeralOwner", "0x" + Long.toHexString(stat.getEphemeralOwner()));
    statMap.put("dataLength", stat.getDataLength());
    statMap.put("numChildren", stat.getNumChildren());

    return statMap;
  }

}

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

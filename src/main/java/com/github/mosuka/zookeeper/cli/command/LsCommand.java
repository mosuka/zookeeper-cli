package com.github.mosuka.zookeeper.cli.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.mosuka.zookeeper.cli.util.StatUtil;
import com.github.mosuka.zookeeper.cli.util.ZooKeeperConnection;

public class LsCommand extends Command {
  public LsCommand() {
    this("ls");
  }

  public LsCommand(String name) {
    super(name);
  }

  @Override
  public void execute(Map<String, Object> parameters) {
    int exitCode = 0;
    String message = "OK";
    List<String> children = new ArrayList<String>();
    Map<String, Object> statMap = new LinkedHashMap<String, Object>();

    String zookeeperServer = (String) parameters.get("zookeeper_server");
    int sessionTimeout = Integer.parseInt((String) parameters.get("session_timeout"));
    String path = (String) parameters.get("path");
    boolean watch = Boolean.parseBoolean((String) parameters.get("watch"));
    boolean withStat = Boolean.parseBoolean((String) parameters.get("with_stat"));

    ZooKeeperConnection zookeeperConnection = null;
    try {
      zookeeperConnection = new ZooKeeperConnection();
      ZooKeeper zookeeper = zookeeperConnection.connect(zookeeperServer, sessionTimeout);

      if (withStat) {
        // with stat
        Stat stat = new Stat();
        children = zookeeper.getChildren(path, watch, stat);
        statMap = StatUtil.stat2Map(stat);
      } else {
        // without stat
        children = zookeeper.getChildren(path, watch);
      }
    } catch (IOException e) {
      exitCode = 1;
      message = e.getMessage();
    } catch (KeeperException e) {
      exitCode = 1;
      message = e.getMessage();
    } catch (InterruptedException e) {
      exitCode = 1;
      message = e.getMessage();
    } catch (Exception e) {
      exitCode = 1;
      message = e.getMessage();
    } finally {
      try {
        if (zookeeperConnection != null) {
          zookeeperConnection.close();
        }
      } catch (InterruptedException e) {
        exitCode = 1;
        message = e.getMessage();
      }
    }

    Map<String, Object> requestMap = new LinkedHashMap<String, Object>();
    requestMap.put("command", getName());
    requestMap.put("parameters", parameters);

    Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
    responseMap.put("status", exitCode);
    responseMap.put("message", message);
    responseMap.put("children", children);
    if (!statMap.isEmpty()) {
      responseMap.put("stat", statMap);
    }

    String resultJSON = null;
    try {
      Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
      resultMap.put("request", requestMap);
      resultMap.put("response", responseMap);
      ObjectMapper mapper = new ObjectMapper();
      resultJSON = mapper.writeValueAsString(resultMap);
    } catch (IOException e) {
      exitCode = 1;
      message = e.getMessage();
      resultJSON = String.format("{\"status\":%d, \"message\":\"%s\"}", exitCode, e.getMessage());
    }
    System.out.println(resultJSON);
    System.exit(exitCode);
  }

}

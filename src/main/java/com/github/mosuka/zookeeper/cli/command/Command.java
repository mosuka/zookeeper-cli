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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.mosuka.zookeeper.cli.util.ZooKeeperConnection;

public class Command implements CommandImpl {

  public static final String SUCCESS_MESSAGE = "Success";
  public static final int SUCCESS_EXIT_CODE = 0;
  public static final int ERROR_EXIT_CODE = 1;

  private String name;
  private int exitCode = SUCCESS_EXIT_CODE;
  private String message = SUCCESS_MESSAGE;

  final CountDownLatch connSignal = new CountDownLatch(1);
  private ZooKeeperConnection zookeeperConnection = null;

  private Map<String, Object> requestMap = new LinkedHashMap<String, Object>();
  private Map<String, Object> responseMap = new LinkedHashMap<String, Object>();

  public Command() {
    this("");
  }

  public Command(String name) {
    setName(name);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getExitCode() {
    return exitCode;
  }

  public void setExitCode(int exitCode) {
    this.exitCode = exitCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ZooKeeperConnection getZookeeperConnection() {
    return zookeeperConnection;
  }

  public void addResponse(String key, Object value) {
    responseMap.put(key, value);
  }

  public void connect(String zookeeperServer, int sessionTimeout)
      throws IOException, InterruptedException {
    zookeeperConnection = new ZooKeeperConnection(zookeeperServer, sessionTimeout);
  }

  public void run(Map<String, Object> parameters) {
    setExitCode(SUCCESS_EXIT_CODE);
    setMessage(SUCCESS_MESSAGE);
  }

  public void output(Map<String, Object> parameters)
      throws JsonGenerationException, JsonMappingException, IOException {
    requestMap.put("command", getName());
    requestMap.put("parameters", parameters);

    responseMap.put("exitCode", getExitCode());
    responseMap.put("message", getMessage());

    String resultJSON = null;
    Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
    resultMap.put("request", requestMap);
    resultMap.put("response", responseMap);

    ObjectMapper mapper = new ObjectMapper();
    if ((Boolean) parameters.get("pretty_print")) {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    resultJSON = mapper.writeValueAsString(resultMap);
    System.out.println(resultJSON);
  }

  public void close() throws InterruptedException {
    zookeeperConnection.close();
  }

  @Override
  public void execute(Map<String, Object> parameters) throws Exception {
    try {
      connect((String) parameters.get("zookeeper_server"),
          (Integer) parameters.get("session_timeout"));

      run(parameters);
      close();
    } catch (IOException | InterruptedException e) {
      setExitCode(ERROR_EXIT_CODE);
      setMessage(e.getMessage());
    } finally {
      output(parameters);
      System.exit(getExitCode());
    }
  }

}

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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.mosuka.zookeeper.nicli.util.ZooKeeperConnection;

public class Command implements CommandImpl {
    public static final String DEFAULT_SERVER = "localhost:2181";
    public static final int DEFAULT_TIMEOUT = 3000;
    public static final boolean DEFAULT_WITH_REQUEST = false;
    public static final boolean DEFAULT_PRETTY_PRINT = false;

    public static final String SUCCESS_MESSAGE = "Success";
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_ERROR = 1;

    private String name;
    private int status = STATUS_SUCCESS;
    private String message = SUCCESS_MESSAGE;

    final CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeperConnection zkConnection = null;

    private Map<String, Object> response = new LinkedHashMap<String, Object>();

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZooKeeperConnection getZookeeperConnection() {
        return zkConnection;
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> responseMap) {
        this.response = responseMap;
    }

    public void putResponse(String key, Object value) {
        response.put(key, value);
    }

    public void connect(String server, int timeout) throws IOException, InterruptedException {
        zkConnection = new ZooKeeperConnection(server, timeout);
    }

    public void run(Map<String, Object> parameters) {
        setStatus(STATUS_SUCCESS);
        setMessage(SUCCESS_MESSAGE);
    }

    public void output(Map<String, Object> parameters)
            throws JsonGenerationException, JsonMappingException, IOException {
        output(parameters, DEFAULT_WITH_REQUEST);
    }

    public void output(Map<String, Object> parameters, boolean withRequest)
            throws JsonGenerationException, JsonMappingException, IOException {
        output(parameters, withRequest, DEFAULT_PRETTY_PRINT);
    }

    public void output(Map<String, Object> parameters, boolean withRequest, boolean prettyPrint)
            throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        // put request to result
        if (withRequest) {
            Map<String, Object> request = new LinkedHashMap<String, Object>();
            request.put("command", getName());
            request.put("parameters", parameters);
            result.put("request", request);
        }

        // put response to result
        putResponse("status", getStatus());
        putResponse("message", getMessage());
        result.put("response", getResponse());

        ObjectMapper mapper = new ObjectMapper();
        if (prettyPrint) {
            // enable pretty print
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
        } else {
            System.out.println(mapper.writeValueAsString(result));
        }
    }

    public void close() throws InterruptedException {
        zkConnection.close();
    }

    @Override
    public int execute(Map<String, Object> parameters) throws Exception {
        String server = parameters.containsKey("server") ? (String) parameters.get("server") : DEFAULT_SERVER;
        int timeout = parameters.containsKey("timeout") ? (Integer) parameters.get("timeout") : DEFAULT_TIMEOUT;
        boolean withRequest = parameters.containsKey("with_request") ? (Boolean) parameters.get("with_request")
                : DEFAULT_WITH_REQUEST;
        boolean prettyPrint = parameters.containsKey("pretty_print") ? (Boolean) parameters.get("pretty_print")
                : DEFAULT_PRETTY_PRINT;

        try {
            connect(server, timeout);
            run(parameters);
        } catch (IOException e) {
            setStatus(STATUS_ERROR);
            setMessage(e.getMessage());
        } catch (InterruptedException e) {
            setStatus(STATUS_ERROR);
            setMessage(e.getMessage());
        } finally {
            try {
                close();
            } catch (InterruptedException e) {
                setStatus(STATUS_ERROR);
                setMessage(e.getMessage());
            }
            output(parameters, withRequest, prettyPrint);
        }

        return getStatus();
    }
}

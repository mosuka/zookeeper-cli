package com.github.mosuka.zookeeper.nicli.command;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.github.mosuka.zookeeper.nicli.util.ZooKeeperTestBase;

public class AddAuthCommandTest extends ZooKeeperTestBase {
    @Test
    public void testExecute() {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("server", zkServerStr);
        parameters.put("session_timeout", sessionTimeout);
        parameters.put("pretty_print", false);
        parameters.put("scheme", "digest");
        parameters.put("auth", "test:test");

        int statusCode = Command.STATUS_SUCCESS;

        AddAuthCommand command = new AddAuthCommand();
        try {
            statusCode = command.execute(parameters);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(Command.STATUS_SUCCESS, statusCode);
    }
}

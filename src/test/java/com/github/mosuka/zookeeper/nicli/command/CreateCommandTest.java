package com.github.mosuka.zookeeper.nicli.command;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.github.mosuka.zookeeper.nicli.util.ZooKeeperTestBase;

public class CreateCommandTest extends ZooKeeperTestBase {
    @Test
    public void testExecute() {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("zookeeper_server", zkServerStr);
        parameters.put("session_timeout", sessionTimeout);
        parameters.put("pretty_print", false);
        parameters.put("path", "/test");
        parameters.put("data", "This is test.");
        parameters.put("acl", "");
        parameters.put("ephemeral", false);
        parameters.put("sequential", false);
        parameters.put("parents", false);
        parameters.put("watch", false);

        int statusCode = Command.STATUS_SUCCESS;

        CreateCommand command = new CreateCommand();
        try {
            statusCode = command.execute(parameters);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(Command.STATUS_SUCCESS, statusCode);
    }
}

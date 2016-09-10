package com.github.mosuka.zookeeper.nicli.command;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.github.mosuka.zookeeper.nicli.util.ZooKeeperTestBase;

public class DeleteCommandTest extends ZooKeeperTestBase {
    @Test
    public void testExecute() {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("zookeeper_server", zkServerStr);
        parameters.put("session_timeout", sessionTimeout);
        parameters.put("pretty_print", false);
        parameters.put("path", "/");
        parameters.put("version", -1);
        parameters.put("recursive", false);

        int statusCode = Command.STATUS_SUCCESS;

        DeleteCommand command = new DeleteCommand();
        try {
            statusCode = command.execute(parameters);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(Command.STATUS_SUCCESS, statusCode);
    }
}

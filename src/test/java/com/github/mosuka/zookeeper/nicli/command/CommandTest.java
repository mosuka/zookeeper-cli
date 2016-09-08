package com.github.mosuka.zookeeper.nicli.command;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.github.mosuka.zookeeper.nicli.command.Command;
import com.github.mosuka.zookeeper.nicli.util.ZooKeeperTestBase;

public class CommandTest extends ZooKeeperTestBase {
    @Test
    public void testExecute() {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("zookeeper_server", zkServerStr);
        parameters.put("session_timeout", sessionTimeout);
        parameters.put("pretty_print", false);

        int statusCode = 0;

        Command command = new Command("test");
        try {
            statusCode = command.execute(parameters);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(0, statusCode);
    }
}

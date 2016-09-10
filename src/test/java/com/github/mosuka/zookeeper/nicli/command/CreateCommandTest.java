package com.github.mosuka.zookeeper.nicli.command;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.github.mosuka.zookeeper.nicli.util.ZooKeeperConnection;
import com.github.mosuka.zookeeper.nicli.util.ZooKeeperTestBase;

public class CreateCommandTest extends ZooKeeperTestBase {
    public ZooKeeperConnection zkConnection = null;

    @Override
    public void before() throws Exception {
        super.before();
        zkConnection = new ZooKeeperConnection(server, sessionTimeout);
    }

    @Override
    public void after() throws Exception {
        super.after();
        zkConnection.close();
    }

    @Test
    public void testExecute() {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("server", server);
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

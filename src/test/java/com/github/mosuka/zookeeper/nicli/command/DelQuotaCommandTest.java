package com.github.mosuka.zookeeper.nicli.command;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.github.mosuka.zookeeper.nicli.util.ZooKeeperConnection;
import com.github.mosuka.zookeeper.nicli.util.ZooKeeperTestBase;

public class DelQuotaCommandTest extends ZooKeeperTestBase {
    public ZooKeeperConnection zkConnection = null;

    @Override
    public void before() throws Exception {
        super.before();
        zkConnection = new ZooKeeperConnection(server, sessionTimeout);

        // prepare the data
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

        CreateCommand command = new CreateCommand();
        command.execute(parameters);

        Map<String, Object> parameters2 = new LinkedHashMap<String, Object>();
        parameters2.put("server", server);
        parameters2.put("session_timeout", sessionTimeout);
        parameters2.put("pretty_print", false);
        parameters2.put("path", "/test");
        parameters2.put("bytes", 10L);
        parameters2.put("num_nodes", -1);

        SetQuotaCommand command2 = new SetQuotaCommand();
        command2.execute(parameters2);
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
        parameters.put("bytes", true);
        parameters.put("num_nodes", true);

        int statusCode = Command.STATUS_SUCCESS;

        DelQuotaCommand command = new DelQuotaCommand();
        try {
            statusCode = command.execute(parameters);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(Command.STATUS_SUCCESS, statusCode);
    }
}

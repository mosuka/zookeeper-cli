package com.github.mosuka.zookeeper.nicli.command;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.github.mosuka.zookeeper.nicli.util.ZooKeeperConnection;
import com.github.mosuka.zookeeper.nicli.util.ZooKeeperTestBase;

public class LsCommandTest extends ZooKeeperTestBase {
    public ZooKeeperConnection zkConnection = null;

    @Override
    public void before() throws Exception {
        super.before();
        zkConnection = new ZooKeeperConnection(server, timeout);
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
        parameters.put("timeout", timeout);
        parameters.put("path", "/");
        parameters.put("watch", false);
        parameters.put("with_stat", false);

        int statusCode = Command.STATUS_SUCCESS;

        LsCommand command = new LsCommand();
        try {
            statusCode = command.execute(parameters);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(Command.STATUS_SUCCESS, statusCode);
    }
}

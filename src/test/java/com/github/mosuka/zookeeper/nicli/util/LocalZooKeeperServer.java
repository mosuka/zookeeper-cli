package com.github.mosuka.zookeeper.nicli.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

import org.apache.zookeeper.server.DatadirCleanupManager;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalZooKeeperServer implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(LocalZooKeeperServer.class);

    private static int DEFAULT_PORT = 2181;

    private int port;
    private File dataDir;
    private DatadirCleanupManager cleanupManager;
    private ZooKeeperServer zkServer;
    private FileTxnSnapLog transactionLog;
    private ServerCnxnFactory connectionFactory;
    private boolean closed;

    public LocalZooKeeperServer() throws IOException {
        this(getFreePort());
    }

    public LocalZooKeeperServer(int port) throws IOException {
        setPort(port);
        File dataDir = new File(System.getProperty("java.io.tmpdir"));
        setDataDir(dataDir);
        getDataDir().deleteOnExit();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public File getDataDir() {
        return dataDir;
    }

    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    private static int getFreePort() throws IOException {
        ServerSocket socket = null;
        int freePort = DEFAULT_PORT;
        try {
            socket = new ServerSocket(0, 0);
            freePort = socket.getLocalPort();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
        return freePort;
    }

    private void clean(File root) throws IOException {
        if (root != null && root.exists()) {
            if (root.isFile()) {
                if (root.exists()) {
                    root.delete();
                }
            } else if (root.isDirectory()) {
                File[] files = root.listFiles();
                for (int i = 0; i < files.length; i++) {
                    clean(files[i]);
                }
                if (root.exists()) {
                    root.delete();
                }
            }
        }
    }

    public void start() throws IOException, ConfigException, InterruptedException {
        log.info("Starting Zookeeper on port {}", port);

        Properties properties = new Properties();
        properties.setProperty("dataDir", getDataDir().getAbsolutePath());
        properties.setProperty("clientPort", Integer.toString(getPort()));

        QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
        quorumConfig.parseProperties(properties);

        cleanupManager = new DatadirCleanupManager(quorumConfig.getDataDir(), quorumConfig.getDataLogDir(),
                quorumConfig.getSnapRetainCount(), quorumConfig.getPurgeInterval());
        cleanupManager.start();

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.readFrom(quorumConfig);

        zkServer = new ZooKeeperServer();
        zkServer.setTickTime(serverConfig.getTickTime());
        zkServer.setMinSessionTimeout(serverConfig.getMinSessionTimeout());
        zkServer.setMaxSessionTimeout(serverConfig.getMaxSessionTimeout());

        transactionLog = new FileTxnSnapLog(new File(serverConfig.getDataLogDir().toString()),
                new File(serverConfig.getDataDir().toString()));
        zkServer.setTxnLogFactory(transactionLog);

        connectionFactory = ServerCnxnFactory.createFactory();
        connectionFactory.configure(serverConfig.getClientPortAddress(), serverConfig.getMaxClientCnxns());
        connectionFactory.startup(zkServer);
    }

    public void await() throws InterruptedException {
        connectionFactory.join();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing Zookeeper on port {}", port);

        if (isClosed()) {
            return;
        }

        setClosed(true);

        if (connectionFactory != null) {
            connectionFactory.shutdown();
            connectionFactory = null;
        }
        if (zkServer != null) {
            zkServer.shutdown();
            zkServer = null;
        }
        if (transactionLog != null) {
            transactionLog.close();
            transactionLog = null;
        }
        if (cleanupManager != null) {
            cleanupManager.shutdown();
            cleanupManager = null;
        }
        clean(getDataDir());
        setDataDir(null);
    }
}

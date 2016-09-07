package com.github.mosuka.zookeeper.cli.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import junit.framework.Assert;

public class ZooKeeperTestBase extends Assert {

  public static LocalZooKeeperServer localZkServer;

  public String zkServerStr = null;
  public int sessionTimeout = 1000;

  @BeforeClass
  public static void beforeClass() throws Exception {
    localZkServer = new LocalZooKeeperServer();
    localZkServer.start();
  }

  @AfterClass
  public static void afterClass() throws Exception {
    localZkServer.close();
  }

  @Before
  public void before() throws Exception {
    zkServerStr = "localhost:" + Integer.toString(localZkServer.getPort());

  }

  @After
  public void after() throws Exception {
  }

}
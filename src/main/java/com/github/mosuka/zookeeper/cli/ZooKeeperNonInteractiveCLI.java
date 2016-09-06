package com.github.mosuka.zookeeper.cli;

import java.util.Map;

import com.github.mosuka.zookeeper.cli.command.Command;
import com.github.mosuka.zookeeper.cli.command.LsCommand;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class ZooKeeperNonInteractiveCLI {

  public static void main(String[] args) {
    ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("java zookeeper-cli.jar");

    Subparsers commandSubpersers =
        argumentParser.addSubparsers().title("Available Commands").metavar("COMMAND");

    Subparser addCmdSubParser = commandSubpersers.addParser("ls").help("List the znodes.")
        .setDefault("command", new LsCommand());
    addCmdSubParser.addArgument("-z", "--zookeeper-server").setDefault("localhost:2181")
        .help("ZooKeeper host and port.");
    addCmdSubParser.addArgument("-p", "--path").setDefault("/").help("ZooKeeper znode path.");
    addCmdSubParser.addArgument("-w", "--watch").setDefault("false").help("Use watch.");
    addCmdSubParser.addArgument("-s", "--with-stat").setDefault("false").help("With stat.");
    addCmdSubParser.addArgument("-t", "--session-timeout").setDefault("3000")
        .help("Session timeout.");

    try {
      Namespace ns = argumentParser.parseArgs(args);
      Command command = ns.get("command");
      Map<String, Object> parameters = ns.getAttrs();
      parameters.remove("command");
      command.execute(parameters);
    } catch (ArgumentParserException e) {
      argumentParser.handleError(e);
      System.exit(1);
    }
  }

}

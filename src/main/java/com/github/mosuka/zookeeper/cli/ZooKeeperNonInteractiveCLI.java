package com.github.mosuka.zookeeper.cli;

import static net.sourceforge.argparse4j.impl.Arguments.storeTrue;

import java.util.Map;

import com.github.mosuka.zookeeper.cli.command.CommandImpl;
import com.github.mosuka.zookeeper.cli.command.LsCommand;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class ZooKeeperNonInteractiveCLI {

  public static void main(String[] args) {
    /*
     * Main command
     */
    ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("java zookeeper-cli.jar");
    argumentParser.addArgument("-z", "--zookeeper-server").type(String.class)
        .setDefault("localhost:2181").help("specify ZooKeeper host and port. ex) localhost:2181");
    argumentParser.addArgument("-t", "--session-timeout").type(Integer.class).setDefault(3000)
        .help("specify session timeout[ms].");

    /*
     * Sub commands
     */
    Subparsers commandSubpersers =
        argumentParser.addSubparsers().title("Available Commands").metavar("COMMAND");

    /*
     * ls command
     */
    Subparser lsCommandSubParser = commandSubpersers.addParser("ls").help("List the znodes.")
        .setDefault("command", new LsCommand());
    lsCommandSubParser.addArgument("path").metavar("PATH").type(String.class).setDefault("/")
        .help("specify ZooKeeper znode path.");
    lsCommandSubParser.addArgument("-w", "--watch").type(Boolean.class).setDefault(false)
        .action(storeTrue()).help("enable watcher.");
    lsCommandSubParser.addArgument("-s", "--with-stat").type(Boolean.class).setDefault(false)
        .action(storeTrue()).help("gets the stat along with the data.");

    /*
     * execute command
     */
    try {
      Namespace ns = argumentParser.parseArgs(args);
      CommandImpl command = ns.get("command");
      Map<String, Object> parameters = ns.getAttrs();
      parameters.remove("command");
      command.execute(parameters);
    } catch (ArgumentParserException e) {
      argumentParser.handleError(e);
      System.exit(1);
    }
  }

}

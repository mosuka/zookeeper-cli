/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mosuka.zookeeper.nicli;

import static net.sourceforge.argparse4j.impl.Arguments.storeTrue;

import java.util.Map;

import com.github.mosuka.zookeeper.nicli.command.Command;
import com.github.mosuka.zookeeper.nicli.command.CommandImpl;
import com.github.mosuka.zookeeper.nicli.command.CreateCommand;
import com.github.mosuka.zookeeper.nicli.command.LsCommand;
import com.github.mosuka.zookeeper.nicli.command.StatCommand;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class ZooKeeperNonInteractiveCLI {
    public static void main(String[] args) {
        ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("java zookeeper-cli.jar");
        argumentParser.addArgument("-z", "--zookeeper-server").type(String.class)
                .setDefault(Command.DEFAULT_ZOOKEEPER_SERVER)
                .help("specify ZooKeeper host and port. ex) localhost:2181");
        argumentParser.addArgument("-t", "--session-timeout").type(Integer.class)
                .setDefault(Command.DEFAULT_SESSION_TIMEOUT).help("specify session timeout[ms].");
        argumentParser.addArgument("-p", "--pretty-print").type(Boolean.class).setDefault(Command.DEFAULT_PRETTY_PRINT)
                .action(storeTrue()).help("pretty print.");

        Subparsers subpersers = argumentParser.addSubparsers().title("Available Commands").metavar("COMMAND");

        /*
         * create command
         */
        Subparser createCommandSubparser = subpersers.addParser("create").help("create the znode.")
                .setDefault("command", new CreateCommand());
        createCommandSubparser.addArgument("path").metavar("PATH").type(String.class)
                .setDefault(CreateCommand.DEFAULT_PATH).help("specify ZooKeeper znode path.");
        createCommandSubparser.addArgument("data").metavar("DATA").type(String.class)
                .setDefault(CreateCommand.DEFAULT_DATA).nargs("?")
                .help("specify the data to be registered in the znode.");
        createCommandSubparser.addArgument("acl").metavar("ACL").type(String.class)
                .setDefault(CreateCommand.DEFAULT_ACL).nargs("?")
                .help("specify the access control list to be registered in the znode.");
        createCommandSubparser.addArgument("-e", "--ephemeral").type(Boolean.class)
                .setDefault(CreateCommand.DEFAULT_EPHEMERAL).action(storeTrue()).help("set create mode to ephemeral.");
        createCommandSubparser.addArgument("-s", "--sequential").type(Boolean.class)
                .setDefault(CreateCommand.DEFAULT_SEQUENTIAL).action(storeTrue())
                .help("set create mode to sequential.");
        createCommandSubparser.addArgument("-c", "--container").type(Boolean.class)
                .setDefault(CreateCommand.DEFAULT_CONTAINER).action(storeTrue()).help("set create mode to container.");

        /*
         * ls command
         */
        Subparser lsCommandSubparser = subpersers.addParser("ls").help("list the znodes.").setDefault("command",
                new LsCommand());
        lsCommandSubparser.addArgument("path").metavar("PATH").type(String.class).setDefault(LsCommand.DEFAULT_PATH)
                .help("specify ZooKeeper znode path.");
        lsCommandSubparser.addArgument("-w", "--watch").type(Boolean.class).setDefault(LsCommand.DEFAULT_WATCH)
                .action(storeTrue()).help("enable watcher.");
        lsCommandSubparser.addArgument("-s", "--with-stat").type(Boolean.class).setDefault(LsCommand.DEFAULT_WITH_STAT)
                .action(storeTrue()).help("gets the stat along with the data.");

        /*
         * stat command
         */
        Subparser statCommandSubparser = subpersers.addParser("stat").help("show stats of the znode.")
                .setDefault("command", new StatCommand());
        statCommandSubparser.addArgument("path").metavar("PATH").type(String.class).setDefault(StatCommand.DEFAULT_PATH)
                .help("specify ZooKeeper znode path.");
        statCommandSubparser.addArgument("-w", "--watch").type(Boolean.class).setDefault(StatCommand.DEFAULT_WATCH)
                .action(storeTrue()).help("gets the stat along with the data.");

        /*
         * execute command
         */
        int status = Command.STATUS_SUCCESS;
        try {
            Namespace namespace = argumentParser.parseArgs(args);
            CommandImpl command = namespace.get("command");
            Map<String, Object> parameters = namespace.getAttrs();
            parameters.remove("command");
            status = command.execute(parameters);
        } catch (ArgumentParserException e) {
            argumentParser.handleError(e);
            status = Command.STATUS_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            status = Command.STATUS_ERROR;
        } finally {
            System.exit(status);
        }
    }
}

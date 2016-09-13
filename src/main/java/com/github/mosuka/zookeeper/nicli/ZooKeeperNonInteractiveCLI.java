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

import com.github.mosuka.zookeeper.nicli.command.AddAuthCommand;
import com.github.mosuka.zookeeper.nicli.command.Command;
import com.github.mosuka.zookeeper.nicli.command.CommandImpl;
import com.github.mosuka.zookeeper.nicli.command.CreateCommand;
import com.github.mosuka.zookeeper.nicli.command.DelQuotaCommand;
import com.github.mosuka.zookeeper.nicli.command.DeleteCommand;
import com.github.mosuka.zookeeper.nicli.command.GetAclCommand;
import com.github.mosuka.zookeeper.nicli.command.GetCommand;
import com.github.mosuka.zookeeper.nicli.command.ListQuotaCommand;
import com.github.mosuka.zookeeper.nicli.command.LsCommand;
import com.github.mosuka.zookeeper.nicli.command.SetAclCommand;
import com.github.mosuka.zookeeper.nicli.command.SetCommand;
import com.github.mosuka.zookeeper.nicli.command.SetQuotaCommand;
import com.github.mosuka.zookeeper.nicli.command.StatCommand;
import com.github.mosuka.zookeeper.nicli.command.SyncCommand;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class ZooKeeperNonInteractiveCLI {
    public static void main(String[] args) {
        ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("java zookeeper-cli.jar");
        argumentParser.addArgument("-s", "--server").type(String.class).setDefault(Command.DEFAULT_SERVER)
                .help("specify ZooKeeper host and port. ex) localhost:2181");
        argumentParser.addArgument("-t", "--timeout").type(Integer.class).setDefault(Command.DEFAULT_TIMEOUT)
                .help("specify session timeout[ms].");
        argumentParser.addArgument("-r", "--with-request").type(Boolean.class).setDefault(Command.DEFAULT_WITH_REQUEST)
                .action(storeTrue()).help("includes the request along with the data.");
        argumentParser.addArgument("-p", "--pretty-print").type(Boolean.class).setDefault(Command.DEFAULT_PRETTY_PRINT)
                .action(storeTrue()).help("pretty print.");

        Subparsers subpersers = argumentParser.addSubparsers().title("Available Commands").metavar("COMMAND");

        /*
         * ls command
         */
        Subparser lsCommandSubparser = subpersers.addParser("ls").help("list the znodes.").setDefault("command",
                new LsCommand("ls"));
        lsCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        lsCommandSubparser.addArgument("-w", "--watch").type(Boolean.class).setDefault(LsCommand.DEFAULT_WATCH)
                .action(storeTrue()).help("enable watcher.");
        lsCommandSubparser.addArgument("-s", "--with-stat").type(Boolean.class).setDefault(LsCommand.DEFAULT_WITH_STAT)
                .action(storeTrue()).help("gets the stat along with the data.");

        /*
         * stat command
         */
        Subparser statCommandSubparser = subpersers.addParser("stat").help("show stats of the znode.")
                .setDefault("command", new StatCommand("stat"));
        statCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        statCommandSubparser.addArgument("-w", "--watch").type(Boolean.class).setDefault(StatCommand.DEFAULT_WATCH)
                .action(storeTrue()).help("gets the stat along with the data.");

        /*
         * create command
         */
        Subparser createCommandSubparser = subpersers.addParser("create").help("create the znode.")
                .setDefault("command", new CreateCommand());
        createCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
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
        createCommandSubparser.addArgument("-p", "--parents").type(Boolean.class)
                .setDefault(CreateCommand.DEFAULT_PARENTS).action(storeTrue())
                .help("Create intermediate znode as required.");
        createCommandSubparser.addArgument("-w", "--watch").type(Boolean.class).setDefault(CreateCommand.DEFAULT_WATCH)
                .action(storeTrue()).help("enable watcher.");

        /*
         * delete
         */
        Subparser deleteCommandSubparser = subpersers.addParser("delete").help("delete the znodes.")
                .setDefault("command", new DeleteCommand("delete"));
        deleteCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        deleteCommandSubparser.addArgument("-v", "--version").type(Integer.class)
                .setDefault(DeleteCommand.DEFAULT_VERSION).help("specify the znode version.");
        deleteCommandSubparser.addArgument("-r", "--recursive").type(Boolean.class)
                .setDefault(DeleteCommand.DEFAULT_RECURSIVE).action(storeTrue()).help("delete the znode recursively.");

        /*
         * get command
         */
        Subparser getCommandSubparser = subpersers.addParser("get").help("get the znodes.").setDefault("command",
                new GetCommand("get"));
        getCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        getCommandSubparser.addArgument("-w", "--watch").type(Boolean.class).setDefault(GetCommand.DEFAULT_WATCH)
                .action(storeTrue()).help("enable watcher.");
        getCommandSubparser.addArgument("-s", "--with-stat").type(Boolean.class)
                .setDefault(GetCommand.DEFAULT_WITH_STAT).action(storeTrue())
                .help("gets the stat along with the data.");

        /*
         * set command
         */
        Subparser setCommandSubparser = subpersers.addParser("set").help("set the znodes.").setDefault("command",
                new SetCommand("set"));
        setCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        setCommandSubparser.addArgument("data").metavar("DATA").type(String.class)
                .help("specify the data to be registered in the znode.");
        setCommandSubparser.addArgument("-v", "--version").type(Integer.class).setDefault(SetCommand.DEFAULT_VERSION)
                .help("specify the znode version.");
        setCommandSubparser.addArgument("-s", "--with-stat").type(Boolean.class)
                .setDefault(SetCommand.DEFAULT_WITH_STAT).action(storeTrue())
                .help("gets the stat along with the data.");

        /*
         * sync command
         */
        Subparser syncCommandSubparser = subpersers.addParser("sync").help("sync the znode.").setDefault("command",
                new SyncCommand("sync"));
        syncCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");

        /*
         * getacl command
         */
        Subparser getAclCommandSubparser = subpersers.addParser("getacl").help("get ACL of the znode.")
                .setDefault("command", new GetAclCommand("getacl"));
        getAclCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        getAclCommandSubparser.addArgument("-s", "--with-stat").type(Boolean.class)
                .setDefault(GetAclCommand.DEFAULT_WITH_STAT).action(storeTrue())
                .help("gets the stat along with the data.");

        /*
         * setacl command
         */
        Subparser setAclCommandSubparser = subpersers.addParser("setacl").help("set ACL of the znode.")
                .setDefault("command", new GetAclCommand("setacl"));
        setAclCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        setAclCommandSubparser.addArgument("acl").metavar("ACL").type(String.class)
                .help("specify the access control list to be registered in the znode.");
        setAclCommandSubparser.addArgument("-v", "--version").type(Integer.class)
                .setDefault(SetAclCommand.DEFAULT_VERSION).help("specify the znode version.");
        setAclCommandSubparser.addArgument("-s", "--with-stat").type(Boolean.class)
                .setDefault(SetAclCommand.DEFAULT_WITH_STAT).action(storeTrue())
                .help("gets the stat along with the data.");

        /*
         * addauth command
         */
        Subparser addAuthCommandSubparser = subpersers.addParser("addauth").help("add auth.").setDefault("command",
                new AddAuthCommand("addauth"));
        addAuthCommandSubparser.addArgument("scheme").metavar("SCHEME").type(String.class).help("specify the scheme.");
        addAuthCommandSubparser.addArgument("auth").metavar("AUTH").type(String.class).help("specify the auth.");

        /*
         * listquota command
         */
        Subparser listQuotaCommandSubparser = subpersers.addParser("listquota").help("list quota.")
                .setDefault("command", new ListQuotaCommand("listquota"));
        listQuotaCommandSubparser.addArgument("path").metavar("PATH").type(String.class)
                .help("specify the znode path.");

        /*
         * setquota command
         */
        Subparser setQuotaCommandSubparser = subpersers.addParser("setquota").help("set quota.").setDefault("command",
                new SetQuotaCommand("setquota"));
        setQuotaCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        MutuallyExclusiveGroup groupSetQuota = setQuotaCommandSubparser.addMutuallyExclusiveGroup().required(true);
        groupSetQuota.addArgument("-b", "--bytes").type(Long.class).setDefault(SetQuotaCommand.DEFAULT_BYTES)
                .help("specify the bytes quota.");
        groupSetQuota.addArgument("-n", "--num-nodes").type(Integer.class).setDefault(SetQuotaCommand.DEFAULT_NUM_NODES)
                .help("specify the num quota.");

        /*
         * delquota command
         */
        Subparser delQuotaCommandSubparser = subpersers.addParser("delquota").help("delete quota.")
                .setDefault("command", new DelQuotaCommand("delquota"));
        delQuotaCommandSubparser.addArgument("path").metavar("PATH").type(String.class).help("specify the znode path.");
        delQuotaCommandSubparser.addArgument("-b", "--bytes").type(Boolean.class)
                .setDefault(DelQuotaCommand.DEFAULT_BYTES).action(storeTrue()).help("delete quota by bytes.");
        delQuotaCommandSubparser.addArgument("-n", "--num-nodes").type(Boolean.class)
                .setDefault(DelQuotaCommand.DEFAULT_NUM_NODES).action(storeTrue()).help("delete quota by num nodes.");

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

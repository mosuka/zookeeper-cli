# zookeeper-cli

zookeeper-cli is a non-interactive command line client for [ZooKeeper](http://zookeeper.apache.org/).


## How to build this project

### Show all targets

```
$ ant -p
Buildfile: /Users/mosuka/git/zookeeper-cli/build.xml

Main targets:

 clean    Clean project.
 compile  Compile project.
 jar      Create JAR files.
 package  Create package.
 resolve  Resolve dependencies.
 run      Run application.
 test     Test project.
Default target: compile
```

### Create package

```
$ ant package
```

### install package

Please extract package/zookeeper-cli-0.1.0.tgz in any directory.


## Show help

```
$ ./bin/zkNiCli.sh -h
usage: java zookeeper-cli.jar [-h] [-s SERVER] [-t SESSION_TIMEOUT] [-p] COMMAND ...

optional arguments:
  -h, --help             show this help message and exit
  -s SERVER, --server SERVER
                         specify ZooKeeper host and port. ex) localhost:2181
  -t SESSION_TIMEOUT, --session-timeout SESSION_TIMEOUT
                         specify session timeout[ms].
  -p, --pretty-print     pretty print.

Available Commands:
  COMMAND
    ls                   list the znodes.
    stat                 show stats of the znode.
    create               create the znode.
    delete               delete the znodes.
    get                  get the znodes.
    set                  set the znodes.
    sync                 sync the znode.
    getacl               get ACL of the znode.
    setacl               set ACL of the znode.
    addauth              add auth.
    listquota            list quota.
    setquota             set quota.
    delquota             delete quota.
```

### Show help for subcommands

This is an example for ls command help.

```
$ ./bin/zkNiCli.sh ls -h
usage: java zookeeper-cli.jar ls [-h] [-w] [-s] [PATH]

positional arguments:
  PATH                   specify the znode path.

optional arguments:
  -h, --help             show this help message and exit
  -w, --watch            enable watcher.
  -s, --with-stat        gets the stat along with the data.
```

## How to use this command

This is an example for ls command.

```
$ ./bin/zkNiCli.sh -p ls -s /
{
  "request" : {
    "command" : "ls",
    "parameters" : {
      "server" : "localhost:2181",
      "path" : "/",
      "watch" : false,
      "pretty_print" : true,
      "with_stat" : true,
      "session_timeout" : 3000
    }
  },
  "response" : {
    "children" : [ "zookeeper" ],
    "stat" : {
      "cZxid" : "0x0",
      "ctime" : "Thu Jan 01 09:00:00 JST 1970",
      "mZxid" : "0x0",
      "mtime" : "Thu Jan 01 09:00:00 JST 1970",
      "pZxid" : "0x330",
      "cversion" : 29,
      "dataVersion" : 0,
      "aclVersion" : 0,
      "ephemeralOwner" : "0x0",
      "dataLength" : 0,
      "numChildren" : 1
    },
    "status" : 0,
    "message" : "Success"
  }
}
```
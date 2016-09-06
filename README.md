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
$ ant clean
$ ant resolve
$ ant compile
$ ant test
$ ant jar
$ ant package
```

### install package

```

```

### Show help

```
$ ./bin/zkCli.sh -h
usage: java zookeeper-cli.jar [-h] [-z ZOOKEEPER_SERVER] [-t SESSION_TIMEOUT] COMMAND ...

optional arguments:
  -h, --help             show this help message and exit
  -z ZOOKEEPER_SERVER, --zookeeper-server ZOOKEEPER_SERVER
                         specify ZooKeeper host and port. ex) localhost:2181
  -t SESSION_TIMEOUT, --session-timeout SESSION_TIMEOUT
                         specify session timeout[ms].

Available Commands:
  COMMAND
    ls                   List the znodes.
```

### Show help for subcommands

```
$ ./bin/zkCli.sh ls -h
usage: java zookeeper-cli.jar ls [-h] [-w] [-s] PATH

positional arguments:
  PATH                   specify ZooKeeper znode path.

optional arguments:
  -h, --help             show this help message and exit
  -w, --watch            enable watcher.
  -s, --with-stat        gets the stat along with the data.
```

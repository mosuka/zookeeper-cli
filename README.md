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

### Show help

```
$ ./bin/zkCli.sh -h
usage: java zookeeper-cli.jar [-h] COMMAND ...

optional arguments:
  -h, --help             show this help message and exit

Available Commands:
  COMMAND
    ls                   List the znodes.
```

### Show help for subcommands

```
$ ./bin/zkCli.sh ls -h
usage: java zookeeper-cli.jar ls [-h] [-z ZOOKEEPER_SERVER] [-p PATH] [-w WATCH] [-s WITH_STAT] [-t SESSION_TIMEOUT]

optional arguments:
  -h, --help             show this help message and exit
  -z ZOOKEEPER_SERVER, --zookeeper-server ZOOKEEPER_SERVER
                         ZooKeeper host and port.
  -p PATH, --path PATH   ZooKeeper znode path.
  -w WATCH, --watch WATCH
                         Use watch.
  -s WITH_STAT, --with-stat WITH_STAT
                         With stat.
  -t SESSION_TIMEOUT, --session-timeout SESSION_TIMEOUT
                         Session timeout.
```

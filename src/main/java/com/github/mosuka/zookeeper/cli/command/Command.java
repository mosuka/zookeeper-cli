package com.github.mosuka.zookeeper.cli.command;

import java.util.Map;

public class Command implements CommandImpl {

  private String name;

  public Command() {
    this("");
  }

  public Command(String name) {
    setName(name);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void execute(Map<String, Object> parameters) {
    System.out.println(getName());
  }

}

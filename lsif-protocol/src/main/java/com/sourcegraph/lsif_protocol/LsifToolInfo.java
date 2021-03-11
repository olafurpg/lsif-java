package com.sourcegraph.lsif_protocol;

public class LsifToolInfo {

  public String name;
  public String version;
  public String[] args;

  public LsifToolInfo() {}

  public LsifToolInfo(String name, String version, String[] args) {
    this.name = name;
    this.version = version;
    this.args = args;
  }
}

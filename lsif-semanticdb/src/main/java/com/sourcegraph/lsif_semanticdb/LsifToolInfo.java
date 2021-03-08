package com.sourcegraph.lsif_semanticdb;

public class LsifToolInfo {
  public final String name;
  public final String version;
  public final String[] args;

  public LsifToolInfo(String name, String version, String[] args) {
    this.name = name;
    this.version = version;
    this.args = args;
  }
}

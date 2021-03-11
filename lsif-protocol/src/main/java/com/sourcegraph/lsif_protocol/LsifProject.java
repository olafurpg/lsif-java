package com.sourcegraph.lsif_protocol;

public class LsifProject extends LsifObject {

  public long id;
  public String type = "vertex";
  public String label = "project";

  public String kind;

  public LsifProject() {}

  public LsifProject(long id, String kind) {
    this.id = id;
    this.kind = kind;
  }
}

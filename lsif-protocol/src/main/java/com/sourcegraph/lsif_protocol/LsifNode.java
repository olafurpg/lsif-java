package com.sourcegraph.lsif_protocol;

public class LsifNode extends LsifObject {

  public long id;
  public String type;
  public String label;

  public LsifNode() {}

  public LsifNode(long id, String type, String label) {
    this.id = id;
    this.type = type;
    this.label = label;
  }
}

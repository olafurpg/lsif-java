package com.sourcegraph.lsif_protocol;

import java.util.List;

public class LsifItemEdge extends LsifObject {

  public long id;
  public String type = "edge";
  public String label = "item";

  public long outV;
  public long[] inVs;
  public long document;

  public LsifItemEdge() {}

  public LsifItemEdge(long id, long outV, long inVs, long document) {
    this.id = id;
    this.outV = outV;
    this.inVs = new long[] {inVs};
    this.document = document;
  }

  @Override
  public String toString() {
    return "LsifItemEdge{"
        + "id="
        + id
        + ", type='"
        + type
        + '\''
        + ", label='"
        + label
        + '\''
        + ", outV="
        + outV
        + ", inVs="
        + inVs
        + ", document="
        + document
        + '}';
  }
}

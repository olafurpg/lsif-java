package com.sourcegraph.lsif_protocol;

import java.util.List;

public class LsifEdge extends LsifObject {

  public long id;
  public String type = "edge";
  public String label;

  public long outV;
  public long inV;

  public LsifEdge() {}

  public LsifEdge(long id, String label, long outV, long inV) {
    this.id = id;
    this.label = label;
    this.outV = outV;
    this.inV = inV;
  }
}

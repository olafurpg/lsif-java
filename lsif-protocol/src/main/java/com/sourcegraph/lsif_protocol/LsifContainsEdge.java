package com.sourcegraph.lsif_protocol;

import java.util.List;

public class LsifContainsEdge extends LsifObject {

  public long id;
  public String type = "edge";
  public String label = "contains";

  public long outV;
  public long[] inV;

  public LsifContainsEdge() {}

  public LsifContainsEdge(long id, long outV, long[] inV) {
    this.id = id;
    this.outV = outV;
    this.inV = inV;
  }
}

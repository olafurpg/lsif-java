package com.sourcegraph.lsif_protocol;

import java.util.List;

public class LsifItemEdge extends LsifObject {

  public long id;
  public String type = "edge";
  public String label = "item";

  public long outV;
  public List<Long> inVs;
  public long document;

  public LsifItemEdge() {}

  public LsifItemEdge(long id, long outV, List<Long> inVs, long document) {
    this.id = id;
    this.outV = outV;
    this.inVs = inVs;
    this.document = document;
  }
}

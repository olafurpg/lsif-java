package com.sourcegraph.lsif_protocol;

import java.util.List;

public class LsifMultiEdge {

  public int id;
  public String type;
  public String label;

  public long outV;
  public List<Long> inVs;

  public LsifMultiEdge() {}

  public LsifMultiEdge(int id, String type, String label, long outV, List<Long> inVs) {
    this.id = id;
    this.type = type;
    this.label = label;
    this.outV = outV;
    this.inVs = inVs;
  }
}

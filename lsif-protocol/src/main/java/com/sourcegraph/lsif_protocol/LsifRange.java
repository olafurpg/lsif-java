package com.sourcegraph.lsif_protocol;

import java.util.List;

public class LsifRange extends LsifObject {

  public long id;
  public String type = "vertex";
  public String label = "range";

  public LsifPosition start;
  public LsifPosition end;

  public LsifRange() {}

  public LsifRange(long id, LsifPosition start, LsifPosition end) {
    this.id = id;
    this.start = start;
    this.end = end;
  }
}

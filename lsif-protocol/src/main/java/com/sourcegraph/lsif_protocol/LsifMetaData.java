package com.sourcegraph.lsif_protocol;

import com.jsoniter.annotation.JsonProperty;
import com.jsoniter.any.Any;

public class LsifMetaData extends LsifObject {

  public long id;
  public String type = "vertex";
  public String label = "metaData";

  public String version;
  public String projectRoot;
  public String positionEncoding = "utf-16";
  public LsifToolInfo toolInfo;

  public LsifMetaData() {}

  public LsifMetaData(long id, String version, String projectRoot, LsifToolInfo toolInfo) {
    this.id = id;
    this.version = version;
    this.projectRoot = projectRoot;
    this.toolInfo = toolInfo;
  }
}

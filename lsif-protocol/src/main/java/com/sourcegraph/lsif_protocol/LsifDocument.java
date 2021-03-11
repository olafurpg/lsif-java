package com.sourcegraph.lsif_protocol;

public class LsifDocument extends LsifObject {

  public long id;
  public String type = "vertex";
  public String label = "document";

  public String uri;
  public String languageId;

  public LsifDocument() {}

  public LsifDocument(long id, String uri, String languageId) {
    this.id = id;
    this.uri = uri;
    this.languageId = languageId;
  }
}

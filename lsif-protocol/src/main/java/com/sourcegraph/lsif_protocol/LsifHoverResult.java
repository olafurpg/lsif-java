package com.sourcegraph.lsif_protocol;

import java.util.List;

public class LsifHoverResult extends LsifObject {

  public long id;
  public String type = "vertex";
  public String label = "hoverResult";

  public Result result;

  public LsifHoverResult() {}

  public LsifHoverResult(long id, Result result) {
    this.id = id;
    this.result = result;
  }

  public static class Result {

    public List<Content> contents;

    public Result() {}

    public Result(List<Content> contents) {
      this.contents = contents;
    }
  }

  public static class Content {
    public String language;
    public String value;

    public Content() {}

    public Content(String language, String value) {
      this.language = language;
      this.value = value;
    }
  }
}

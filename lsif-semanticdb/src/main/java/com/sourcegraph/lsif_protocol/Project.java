package com.sourcegraph.lsif_protocol;

import com.jsoniter.annotation.JsonProperty;
import com.jsoniter.any.Any;

public class Project {

  @JsonProperty(nullable = false)
  public String firstName;

  @JsonProperty(nullable = false)
  public String lastName;

  public int score;
  public Any attachment;
}

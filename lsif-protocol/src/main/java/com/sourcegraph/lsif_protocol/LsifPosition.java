package com.sourcegraph.lsif_protocol;

public class LsifPosition {
  public long line;
  public long character;

  public LsifPosition() {}

  public LsifPosition(long line, long character) {
    this.line = line;
    this.character = character;
  }
}

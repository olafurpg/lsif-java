package com.sourcegraph.lsif_semanticdb;

public abstract class LsifSemanticdbReporter {
  public void error(Throwable e) {}

  public boolean hasErrors() {
    return false;
  }
}

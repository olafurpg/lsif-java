package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb;

import java.nio.file.Path;

public class LsifDocument {
  public final Path path;
  public Semanticdb.TextDocument semanticdb;
  public long id;

  public LsifDocument(Path path) {
    this.path = path;
    this.id = -1;
  }
}

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

  public LsifDocument(Path path, Semanticdb.TextDocument semanticdb) {
    this(path);
    this.semanticdb = semanticdb;
  }

  @Override
  public String toString() {
    return "LsifDocument{" + "path=" + path + ", semanticdb=" + semanticdb + ", id=" + id + '}';
  }
}

package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb;

import java.nio.file.Path;

public class LsifDocument {
  public final Path semanticdbPath;
  public Semanticdb.TextDocument semanticdb;
  public long id;

  public LsifDocument(Path semanticdbPath) {
    this.semanticdbPath = semanticdbPath;
    this.id = -1;
  }

  public LsifDocument(Path semanticdbPath, Semanticdb.TextDocument semanticdb, Path sourceroot) {
    this(semanticdbPath);
    String uri = sourceroot.resolve(semanticdb.getUri()).toUri().toString();
    System.out.println(uri);
    this.semanticdb = Semanticdb.TextDocument.newBuilder(semanticdb).setUri(uri).build();
  }

  @Override
  public String toString() {
    return "LsifDocument{"
        + "path="
        + semanticdbPath
        + ", semanticdb="
        + semanticdb
        + ", id="
        + id
        + '}';
  }
}

package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LsifDocument {
  public final Path semanticdbPath;
  public Semanticdb.TextDocument semanticdb;
  public long id;
  public final Map<String, Semanticdb.SymbolInformation> symbols;

  public LsifDocument(Path semanticdbPath) {
    this.semanticdbPath = semanticdbPath;
    this.id = -1;
    this.symbols = new HashMap<>();
  }

  public LsifDocument(Path semanticdbPath, Semanticdb.TextDocument semanticdb, Path sourceroot) {
    this(semanticdbPath);
    String uri = sourceroot.resolve(semanticdb.getUri()).toUri().toString();
    setSemanticdb(Semanticdb.TextDocument.newBuilder(semanticdb).setUri(uri).build());
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

  private void setSemanticdb(Semanticdb.TextDocument semanticdb) {
    this.semanticdb = semanticdb;
    for (Semanticdb.SymbolInformation info : semanticdb.getSymbolsList()) {
      symbols.put(info.getSymbol(), info);
    }
  }
}

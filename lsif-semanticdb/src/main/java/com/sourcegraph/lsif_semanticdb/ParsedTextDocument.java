package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb;

import java.nio.file.Path;
import java.util.*;

public class ParsedTextDocument {
  public final Path semanticdbPath;
  public Semanticdb.TextDocument semanticdb;
  public long id;
  public final Map<String, Semanticdb.SymbolInformation> symbols;
  public final Map<String, ResultIds> localSymbols;

  public ParsedTextDocument(
      Path semanticdbPath, Semanticdb.TextDocument semanticdb, Path sourceroot) {
    this.semanticdbPath = semanticdbPath;
    this.symbols = new HashMap<>();
    this.localSymbols = new HashMap<>();
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

  public List<Semanticdb.SymbolOccurrence> sortedSymbolOccurrences() {
    ArrayList<Semanticdb.SymbolOccurrence> result =
        new ArrayList<>(semanticdb.getOccurrencesList().size());
    result.addAll(semanticdb.getOccurrencesList());
    result.sort((o1, o2) -> new RangeComparator().compare(o1.getRange(), o2.getRange()));
    return result;
  }

  private void setSemanticdb(Semanticdb.TextDocument semanticdb) {
    this.semanticdb = semanticdb;
    for (Semanticdb.SymbolInformation info : semanticdb.getSymbolsList()) {
      symbols.put(info.getSymbol(), info);
    }
  }
}
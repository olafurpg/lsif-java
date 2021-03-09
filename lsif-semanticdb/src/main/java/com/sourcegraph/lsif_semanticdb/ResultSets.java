package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb;
import com.sourcegraph.semanticdb_javac.SemanticdbSymbols;

import java.util.HashMap;
import java.util.Map;

public class ResultSets {
  private final Map<String, ResultIds> globals;
  private final LsifWriter writer;
  private final HashMap<String, ResultIds> locals;
  private final HashMap<Semanticdb.Range, Long> ranges;

  public ResultSets(Map<String, ResultIds> globals, LsifWriter writer) {
    this.globals = globals;
    this.writer = writer;
    locals = new HashMap<>();
    ranges = new HashMap<>();
  }

  public long getOrInsertRange(Semanticdb.Range range) {
    Long result = ranges.get(range);
    if (result != null) return result;

    result = writer.emitRange(range);
    ranges.put(range, result);

    return result;
  }

  public ResultIds getOrInsertResultSet(String symbol) {
    boolean isLocal = SemanticdbSymbols.isLocal(symbol);
    Map<String, ResultIds> cache = isLocal ? globals : locals;

    return cache.computeIfAbsent(symbol, ignored -> new ResultIds(writer.emitResultSet(), writer));
  }
}

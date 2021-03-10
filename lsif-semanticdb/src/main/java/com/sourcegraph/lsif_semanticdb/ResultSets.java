package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb.Range;
import com.sourcegraph.semanticdb_javac.SemanticdbSymbols;

import java.util.HashMap;
import java.util.Map;

public class ResultSets {

  private final Map<String, ResultIds> globals;
  private final LsifWriter writer;
  private final HashMap<String, ResultIds> locals;
  private final HashMap<Range, Long> ranges;
  private Range lastRange;

  public ResultSets(Map<String, ResultIds> globals, LsifWriter writer) {
    this.globals = globals;
    this.writer = writer;
    locals = new HashMap<>();
    ranges = new HashMap<>();
  }

  public long getOrInsertRange(Range range) {
    Range queryRange = isOverlappingWithLastRange(range) ? lastRange : range;
    Long result = ranges.get(queryRange);
    if (result != null) {
      return result;
    }

    lastRange = range;
    result = writer.emitRange(range);

    ranges.put(range, result);

    return result;
  }

  public ResultIds getOrInsertResultSet(String symbol) {
    boolean isLocal = SemanticdbSymbols.isLocal(symbol);
    Map<String, ResultIds> cache = isLocal ? globals : locals;
    return cache.computeIfAbsent(
        symbol,
        ignored ->
            new ResultIds(
                writer.emitResultSet(),
                writer,
                writer.emitDefinitionResult(),
                writer.emitReferenceResult()));
  }

  private boolean isOverlappingWithLastRange(Range range) {
    // The LSIF spec prohibits overlapping ranges so we merge them:
    // > No two ranges can overlap, claiming the same position in a document unless
    // > one range is entirely contained by the other.
    return lastRange != null
        && lastRange.getStartLine() <= range.getStartLine()
        && lastRange.getStartCharacter() <= range.getStartCharacter()
        && lastRange.getEndLine() >= range.getStartLine()
        && lastRange.getEndCharacter() >= range.getStartLine();
  }
}

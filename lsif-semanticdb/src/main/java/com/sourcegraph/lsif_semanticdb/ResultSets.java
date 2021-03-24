package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.SemanticdbSymbols;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/** Utility to deal with the creation of result sets. */
public class ResultSets implements Function<String, ResultIds> {

  private final LsifWriter writer;
  private final Map<String, ResultIds> globals;
  private final HashMap<String, ResultIds> locals;
  private final Set<String> exportedSymbols;
  private final Set<String> localDefinitions;

  public ResultSets(
      LsifWriter writer,
      Map<String, ResultIds> globals,
      Set<String> exportedSymbols,
      Set<String> localDefinitions) {
    this.writer = writer;
    this.globals = globals;
    this.exportedSymbols = exportedSymbols;
    this.localDefinitions = localDefinitions;
    locals = new HashMap<>();
  }

  public ResultIds getOrInsertResultSet(String symbol) {
    boolean isLocal = SemanticdbSymbols.isLocal(symbol);
    Map<String, ResultIds> cache = isLocal ? locals : globals;
    return cache.computeIfAbsent(symbol, this);
  }

  @Override
  public ResultIds apply(String symbol) {
    boolean isExportedSymbol = exportedSymbols.contains(symbol);
    boolean hasDefinitionResult = isExportedSymbol || localDefinitions.contains(symbol);
    int resultSet = writer.emitResultSet();

    // Moniker
    int monikerId = writer.emitMonikerVertex(symbol, isExportedSymbol);
    writer.emitMonikerEdge(resultSet, monikerId);

    int definitionId = hasDefinitionResult ? writer.emitDefinitionResult(resultSet) : -1;

    return new ResultIds(resultSet, definitionId, writer.emitReferenceResult(resultSet));
  }
}

package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb.*;
import com.sourcegraph.semanticdb_javac.Semanticdb;
import com.sourcegraph.semanticdb_javac.SemanticdbSymbols;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LsifSemanticdb {
  private final LsifWriter writer;
  private final LsifSemanticdbOptions options;
  private final Map<String, ResultIds> globalSymbols;
  private final Map<String, ResultIds> globals;

  public LsifSemanticdb(LsifWriter writer, LsifSemanticdbOptions options) {
    this.writer = writer;
    this.options = options;
    globalSymbols = new HashMap<>();
    this.globals = new ConcurrentHashMap<>();
  }

  public static void run(LsifSemanticdbOptions options) throws IOException {
    LsifWriter writer = new LsifWriter(options);
    new LsifSemanticdb(writer, options).run();
  }

  private void run() throws IOException {
    writer.emitMetaData();
    long projectId = writer.emitProject(options.language);

    List<LsifDocument> files = SemanticdbWalker.findSemanticdbFiles(options);
    if (options.reporter.hasErrors()) return;

    files = files.stream().flatMap(this::parseTextDocument).collect(Collectors.toList());
    if (options.reporter.hasErrors()) return;

    emitDocuments(files.stream(), projectId);
    if (options.reporter.hasErrors()) return;

    files.forEach(this::emit);

    writer.build();
  }

  private void emit(LsifDocument doc) {
    ResultSets results = new ResultSets(globals, writer);
    ArrayList<Long> rangeIds = new ArrayList<>();
    for (SymbolOccurrence occ : doc.semanticdb.getOccurrencesList()) {
      SymbolInformation symbolInformation =
          doc.symbols.getOrDefault(occ.getSymbol(), SymbolInformation.getDefaultInstance());
      ResultIds ids = results.getOrInsertResultSet(occ.getSymbol());
      long rangeId = results.getOrInsertRange(occ.getRange());
      rangeIds.add(rangeId);

      // Definition
      writer.emitNext(rangeId, ids.resultSet);

      // Reference
      writer.emitItem(ids.referenceResult(), rangeId, doc.id);

      if (occ.getRole() == SymbolOccurrence.Role.DEFINITION) { // Definition
        writer.emitItem(ids.definitionResult(), rangeId, doc.id);

        // Hover
        long hoverId =
            writer.emitHoverResult(
                doc.semanticdb.getLanguage(),
                symbolInformation.getDisplayName().isEmpty()
                    ? occ.getSymbol()
                    : symbolInformation.getDisplayName());
        writer.emitHoverEdge(ids.resultSet, hoverId);
      }
    }
    writer.emitContains(doc.id, rangeIds);
  }

  private void emitDocuments(Stream<LsifDocument> files, long projectId) {
    List<Long> ids = files.map(writer::emitDocument).collect(Collectors.toList());
    writer.emitContains(projectId, ids);
  }

  private Stream<LsifDocument> parseTextDocument(LsifDocument doc) {
    try {
      return Semanticdb.TextDocuments.parseFrom(Files.readAllBytes(doc.semanticdbPath))
          .getDocumentsList().stream()
          .filter(sdb -> !sdb.getOccurrencesList().isEmpty())
          .map(sdb -> new LsifDocument(doc.semanticdbPath, sdb, options.sourceroot));
    } catch (IOException e) {
      options.reporter.error(e);
      return Stream.empty();
    }
  }
}

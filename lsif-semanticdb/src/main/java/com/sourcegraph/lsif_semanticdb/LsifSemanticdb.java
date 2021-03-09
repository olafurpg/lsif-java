package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb.*;
import com.sourcegraph.semanticdb_javac.Semanticdb;
import com.sourcegraph.semanticdb_javac.SemanticdbSymbols;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    List<Path> files = SemanticdbWalker.findSemanticdbFiles(options);
    if (options.reporter.hasErrors()) return;

    List<Long> documentIds =
        files.stream()
            .flatMap(this::parseTextDocument)
            .map(this::emit)
            .collect(Collectors.toList());
    writer.emitContains(projectId, documentIds);

    writer.build();
  }

  private Long emit(LsifDocument doc) {
    long documentId = writer.emitDocument(doc);
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
        String documentation = symbolInformation.getDocumentation().getMessage();
        long hoverId = writer.emitHoverResult(doc.semanticdb.getLanguage(), documentation);
        writer.emitHoverEdge(ids.resultSet, hoverId);
      }
    }
    writer.emitContains(doc.id, rangeIds);
    return documentId;
  }

  private Stream<LsifDocument> parseTextDocument(Path semanticdbPath) {
    try {
      return Semanticdb.TextDocuments.parseFrom(Files.readAllBytes(semanticdbPath))
          .getDocumentsList().stream()
          .filter(sdb -> !sdb.getOccurrencesList().isEmpty())
          .map(sdb -> new LsifDocument(semanticdbPath, sdb, options.sourceroot));
    } catch (IOException e) {
      options.reporter.error(e);
      return Stream.empty();
    }
  }
}

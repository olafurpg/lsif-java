package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb.*;
import com.sourcegraph.semanticdb_javac.Semanticdb;
import com.sourcegraph.semanticdb_javac.SemanticdbSymbols;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LsifSemanticdb {
  private final LsifWriter writer;
  private final LsifSemanticdbOptions options;
  private final Map<String, DefinitionInfo> globalSymbols;

  public LsifSemanticdb(LsifWriter writer, LsifSemanticdbOptions options) {
    this.writer = writer;
    this.options = options;
    globalSymbols = new HashMap<>();
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

    files.forEach(this::emitDefinitions);

    files.forEach(this::emitReferences);

    writer.build();
  }

  private void emitReferences(LsifDocument doc) {
    for (SymbolOccurrence occ : doc.semanticdb.getOccurrencesList()) {
      long resultSetId;
      //      if (globalSymbols.containsKey(occ.getSymbol())) {
      //        resultSetId = globalSymbols.get(occ.getSymbol());
      //      } else {
      //        resultSetId = doc.localSymbols.get(occ.getSymbol());
      //      }
      if (occ.getRole() == SymbolOccurrence.Role.REFERENCE) {
        writer.emitDefinitionResult();
      } else if (occ.getRole() == SymbolOccurrence.Role.DEFINITION) {
      }
      // Hover message
      // Definition
      // Reference
    }

    // Emit local + global references
  }

  private void emitDefinitions(LsifDocument doc) {
    for (SymbolOccurrence occ : doc.semanticdb.getOccurrencesList()) {
      if (occ.getRole() == SymbolOccurrence.Role.DEFINITION) {
        long rangeId = writer.emitRange(occ.getRange());
        long resultSetId = writer.emitResultSet();
        writer.emitNext(rangeId, resultSetId);
        long definitionResultId = writer.emitDefinitionResult();
        writer.emitDefinitionEdge(resultSetId, definitionResultId);
        writer.emitItem(definitionResultId, rangeId, doc.id);

        DefinitionInfo info = new DefinitionInfo(doc.id, rangeId, resultSetId, definitionResultId);
        if (SemanticdbSymbols.isGlobal(occ.getSymbol())) {
          globalSymbols.put(occ.getSymbol(), info);
        } else {
          doc.localSymbols.put(occ.getSymbol(), info);
        }

        SymbolInformation sinfo =
            doc.symbols.getOrDefault(occ.getSymbol(), SymbolInformation.getDefaultInstance());
        String documentation = sinfo.getDisplayName();
        long hoverResultId = writer.emitHoverResult(doc.semanticdb.getLanguage(), documentation);
        writer.emitHoverEdge(resultSetId, hoverResultId);
      }
    }
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

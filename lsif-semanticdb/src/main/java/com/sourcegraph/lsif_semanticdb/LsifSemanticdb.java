package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.semanticdb_javac.Semanticdb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LsifSemanticdb {
  private final LsifWriter writer;
  private final LsifSemanticdbOptions options;

  public LsifSemanticdb(LsifWriter writer, LsifSemanticdbOptions options) {
    this.writer = writer;
    this.options = options;
  }

  public static void run(LsifSemanticdbOptions options) throws IOException {
    try (LsifWriter writer = new LsifWriter(options)) {
      new LsifSemanticdb(writer, options).run();
    }
  }

  private void run() throws IOException {
    writer.emitMetaData();
    long projectId = writer.emitProject(options.language);

    Stream<LsifDocument> files = SemanticdbWalker.findSemanticdbFiles(options).parallelStream();
    if (options.reporter.hasErrors()) return;

    files.forEach(this::parseTextDocuments);
    if (options.reporter.hasErrors()) return;

    emitDocuments(files, projectId);

    writer.build();
  }

  private void emitDocuments(Stream<LsifDocument> files, long projectId) {
    List<Long> ids = files.map(writer::emitDocument).collect(Collectors.toList());
    writer.emitContains(projectId, ids);
  }

  private void parseTextDocuments(LsifDocument doc) {
    try {
      Semanticdb.TextDocuments.parseFrom(Files.readAllBytes(doc.path)).getDocumentsList().stream()
          .filter(sdb -> !sdb.getOccurrencesList().isEmpty())
          .forEach(sdb -> doc.semanticdb = sdb);
    } catch (IOException e) {
      options.reporter.error(e);
    }
  }
}

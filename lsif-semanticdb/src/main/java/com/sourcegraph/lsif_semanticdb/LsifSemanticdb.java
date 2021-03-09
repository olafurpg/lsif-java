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

    List<LsifDocument> files = SemanticdbWalker.findSemanticdbFiles(options);
    if (options.reporter.hasErrors()) return;
    System.out.println(files);

    files = files.stream().flatMap(this::parseTextDocument).collect(Collectors.toList());
    if (options.reporter.hasErrors()) return;

    emitDocuments(files.stream(), projectId);
    if (options.reporter.hasErrors()) return;

    writer.build();
  }

  private void emitDocuments(Stream<LsifDocument> files, long projectId) {
    List<Long> ids = files.map(writer::emitDocument).collect(Collectors.toList());
    writer.emitContains(projectId, ids);
  }

  private Stream<LsifDocument> parseTextDocument(LsifDocument doc) {
    try {
      return Semanticdb.TextDocuments.parseFrom(Files.readAllBytes(doc.path)).getDocumentsList()
          .stream()
          .filter(sdb -> !sdb.getOccurrencesList().isEmpty())
          .map(sdb -> new LsifDocument(doc.path, sdb));
    } catch (IOException e) {
      options.reporter.error(e);
      return Stream.empty();
    }
  }
}

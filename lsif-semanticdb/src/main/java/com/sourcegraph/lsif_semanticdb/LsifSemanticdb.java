package com.sourcegraph.lsif_semanticdb;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LsifSemanticdb {
  private final LsifWriter writer;
  private final LsifSemanticdbOptions options;

  public LsifSemanticdb(LsifWriter writer, LsifSemanticdbOptions options) {
    this.writer = writer;
    this.options = options;
  }

  private void run() throws IOException {
    List<Path> files = SemanticdbWalker.findSemanticdbFiles(options);
    if (options.reporter.hasErrors()) return;
    writer.build();
  }

  public static void run(LsifSemanticdbOptions options) throws IOException {
    try (LsifWriter writer = new LsifWriter(options)) {
      new LsifSemanticdb(writer, options).run();
    }
  }
}

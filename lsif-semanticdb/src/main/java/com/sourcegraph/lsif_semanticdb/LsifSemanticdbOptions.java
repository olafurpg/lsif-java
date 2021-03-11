package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.lsif_protocol.LsifToolInfo;
import java.nio.file.Path;
import java.util.List;

public class LsifSemanticdbOptions {

  public final List<Path> targetroots;
  public final Path output;
  public final Path sourceroot;
  public final LsifSemanticdbReporter reporter;
  public final LsifToolInfo toolInfo;
  public final String language;

  public LsifSemanticdbOptions(
      List<Path> targetroots,
      Path output,
      Path sourceroot,
      LsifSemanticdbReporter reporter,
      LsifToolInfo toolInfo,
      String language) {
    this.targetroots = targetroots;
    this.output = output;
    this.sourceroot = sourceroot;
    this.reporter = reporter;
    this.toolInfo = toolInfo;
    this.language = language;
  }
}

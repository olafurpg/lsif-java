package com.sourcegraph.lsif_semanticdb;

public class DefinitionInfo {
  private final long documentId;
  private final long rangeId;
  private final long resultSetId;
  private final long definitionResultId;

  public DefinitionInfo(long documentId, long rangeId, long resultSetId, long definitionResultId) {
    this.documentId = documentId;
    this.rangeId = rangeId;
    this.resultSetId = resultSetId;
    this.definitionResultId = definitionResultId;
  }
}

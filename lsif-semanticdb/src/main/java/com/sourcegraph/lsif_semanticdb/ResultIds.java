package com.sourcegraph.lsif_semanticdb;

public class ResultIds {
  public final long resultSet;
  public final LsifWriter writer;
  private final JsonObjectBuilder definitionResultBuilder;
  private final JsonObjectBuilder referenceResultBuilder;
  private Long definitionResult;
  private Long referenceResult;

  public ResultIds(
      long resultSet,
      LsifWriter writer,
      JsonObjectBuilder definitionResultBuilder,
      JsonObjectBuilder referenceResultBuilder) {
    this.resultSet = resultSet;
    this.writer = writer;
    this.definitionResultBuilder = definitionResultBuilder;
    this.referenceResultBuilder = referenceResultBuilder;
  }

  public long definitionResult() {
    if (definitionResult == null) {
      definitionResult = definitionResultBuilder.id;
      if (definitionResultBuilder.emitBoolean()) {
        writer.emitDefinitionEdge(resultSet, definitionResult);
      }
    }
    return definitionResult;
  }

  public long referenceResult() {
    if (referenceResult == null) {
      referenceResult = referenceResultBuilder.id;
      if (referenceResultBuilder.emitBoolean()) {
        writer.emitReferenceEdge(resultSet, referenceResult);
      }
    }
    return referenceResult;
  }
}

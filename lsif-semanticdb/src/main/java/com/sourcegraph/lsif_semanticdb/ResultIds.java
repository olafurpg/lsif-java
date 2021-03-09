package com.sourcegraph.lsif_semanticdb;

import java.util.concurrent.atomic.AtomicReference;

public class ResultIds {
  public final long resultSet;
  public final LsifWriter writer;
  private Long definitionResult;
  private Long referenceResult;

  public ResultIds(long resultSet, LsifWriter writer) {
    this.resultSet = resultSet;
    this.writer = writer;
  }

  public long definitionResult() {
    if (definitionResult == null) {
      definitionResult = writer.emitDefinitionResult();
      writer.emitDefinitionEdge(resultSet, definitionResult);
    }
    return definitionResult;
  }

  public long referenceResult() {
    if (referenceResult == null) {
      referenceResult = writer.emitReferenceResult();
      writer.emitReferenceEdge(resultSet, referenceResult);
    }
    return referenceResult;
  }
}

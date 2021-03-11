package com.sourcegraph.lsif_semanticdb;

import com.sourcegraph.lsif_protocol.LsifNode;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResultIds {
  public final long resultSet;
  public final LsifWriter writer;
  private final LsifNode definitionNode;
  private final LsifNode referenceNode;
  private final AtomicBoolean definitionEmitted = new AtomicBoolean(false);
  private final AtomicBoolean referenceEmitted = new AtomicBoolean(false);
  private Long definitionResult;
  private Long referenceResult;

  public ResultIds(
      long resultSet, LsifWriter writer, LsifNode definitionNode, LsifNode referenceNode) {
    this.resultSet = resultSet;
    this.writer = writer;
    this.definitionNode = definitionNode;
    this.referenceNode = referenceNode;
  }

  public long definitionResult() {
    if (definitionResult == null) {
      definitionResult = definitionNode.id;
      if (definitionEmitted.compareAndSet(false, true)) {
        writer.emitObject(definitionNode);
        writer.emitDefinitionEdge(resultSet, definitionResult);
      }
    }
    return definitionResult;
  }

  public long referenceResult() {
    if (referenceResult == null) {
      referenceResult = referenceNode.id;
      if (referenceEmitted.compareAndSet(false, true)) {
        writer.emitObject(referenceNode);
        writer.emitDefinitionEdge(resultSet, referenceResult);
      }
    }
    return referenceResult;
  }
}

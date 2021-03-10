package com.sourcegraph.lsif_semanticdb;

import com.google.gson.*;
import com.sourcegraph.semanticdb_javac.Semanticdb;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicLong;

public class LsifWriter implements AutoCloseable {

  private final Path tmp;
  private final PrintStream output;
  private final LsifSemanticdbOptions options;
  private final AtomicLong id;
  private final Gson gson;

  public LsifWriter(LsifSemanticdbOptions options) throws IOException {
    this.tmp = Files.createTempFile("lsif-semanticdb", "dump.lsif");
    this.output =
        new PrintStream(new BufferedOutputStream(Files.newOutputStream(tmp)), false, "utf8");
    this.options = options;
    this.id = new AtomicLong();
    this.gson = new Gson();
  }

  public void emitMetaData() {
    vertex("metaData")
        .putString("version", "0.4.3")
        .putString("projectRoot", options.sourceroot.toUri().toString())
        .putString("positionEncoding", "utf-16")
        .putElement(
            "toolInfo",
            jsonObject()
                .putString("name", options.toolInfo.name)
                .putString("version", options.toolInfo.version)
                .putElement("args", jsonArray(options.toolInfo.args))
                .build())
        .emit();
  }

  public long emitProject(String language) {
    return vertex("project").putString("kind", language).emit();
  }

  public long emitDocument(LsifDocument doc) {
    return vertex("document")
        .putString("uri", doc.semanticdb.getUri())
        .putString("language", doc.semanticdb.getLanguage().toString().toLowerCase())
        .emit();
  }

  public <T extends Number> void emitContains(long outV, Iterable<T> inVs) {
    edge("contains").putNumber("outV", outV).putElement("inVs", jsonArray(inVs)).emit();
  }

  public long emitRange(Semanticdb.Range range) {
    return vertex("range")
        .putElement(
            "start",
            jsonObject()
                .putNumber("line", range.getStartLine())
                .putNumber("character", range.getStartCharacter())
                .build())
        .putElement(
            "end",
            jsonObject()
                .putNumber("line", range.getEndLine())
                .putNumber("character", range.getEndCharacter())
                .build())
        .emit();
  }

  public void build() throws IOException {
    close();
    Files.copy(tmp, options.output, StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public void close() {
    output.flush();
    output.close();
  }

  private JsonObjectBuilder vertex(String label) {
    return new JsonObjectBuilder(nextId(), "vertex", label, gson, output);
  }

  private JsonObjectBuilder edge(String label) {
    return new JsonObjectBuilder(nextId(), "edge", label, gson, output);
  }

  private long nextId() {
    return id.incrementAndGet();
  }

  public long emitResultSet() {
    return vertex("resultSet").emit();
  }

  public void emitNext(long outV, long inV) {
    edge("next").putNumber("outV", outV).putNumber("inV", inV).emit();
  }

  public JsonObjectBuilder emitReferenceResult() {
    return vertex("referenceResult");
  }

  public JsonObjectBuilder emitDefinitionResult() {
    return vertex("definitionResult");
  }

  public void emitDefinitionEdge(long outV, long inV) {
    edge("textDocument/definition").putOutIn(outV, inV).emit();
  }

  public void emitReferenceEdge(long outV, long inV) {
    edge("textDocument/reference").putOutIn(outV, inV).emit();
  }

  public long emitHoverResult(Semanticdb.Language language, String value) {
    return vertex("hoverResult")
        .putElement(
            "result",
            jsonObject()
                .putElement(
                    "contents",
                    jsonArray(
                        jsonObject()
                            .putString("language", language.toString().toLowerCase())
                            .putString("value", value)
                            .build()))
                .build())
        .emit();
  }

  public void emitHoverEdge(long outV, long inV) {
    edge("textDocument/hover").putOutIn(outV, inV).emit();
  }

  public void emitItem(long outV, long inV, long document) {
    edge("item")
        .putNumber("outV", outV)
        .putElement("inVs", jsonArray(new JsonPrimitive(inV)))
        .putNumber("document", document)
        .emit();
  }

  private JsonArray jsonArray(JsonElement value) {
    JsonArray array = new JsonArray();
    array.add(value);
    return array;
  }

  private JsonArray jsonArray(JsonElement[] values) {
    JsonArray array = new JsonArray();
    for (JsonElement value : values) {
      array.add(value);
    }
    return array;
  }

  private <T extends Number> JsonArray jsonArray(Iterable<T> values) {
    JsonArray array = new JsonArray();
    for (Number value : values) {
      array.add(value);
    }
    return array;
  }

  private JsonArray jsonArray(String[] values) {
    JsonArray array = new JsonArray();
    for (String value : values) {
      array.add(value);
    }
    return array;
  }

  private JsonObjectBuilder jsonObject() {
    return new JsonObjectBuilder(gson, output);
  }
}

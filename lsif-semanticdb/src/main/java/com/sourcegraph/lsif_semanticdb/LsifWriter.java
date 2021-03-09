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
  private final Gson gson;
  private final AtomicLong id;

  public LsifWriter(LsifSemanticdbOptions options) throws IOException {
    this.tmp = Files.createTempFile("lsif-semanticdb", "dump.lsif");
    this.output =
        new PrintStream(new BufferedOutputStream(Files.newOutputStream(tmp)), false, "utf8");
    this.options = options;
    this.gson = new Gson();
    this.id = new AtomicLong();
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
    long docId =
        vertex("document")
            .putString("uri", doc.semanticdb.getUri())
            .putString("language", doc.semanticdb.getLanguage().toString().toLowerCase())
            .emit();
    doc.id = docId;
    return docId;
  }

  public <T extends Number> void emitContains(long outV, Iterable<T> inVs) {
    edge("contains").putLong("outV", outV).putElement("inVs", jsonArray(inVs)).emit();
  }

  public long emitRange(Semanticdb.Range range) {
    return vertex("range")
        .putElement(
            "start",
            jsonObject()
                .putLong("line", range.getStartLine())
                .putLong("character", range.getStartCharacter())
                .build())
        .putElement(
            "end",
            jsonObject()
                .putLong("line", range.getEndLine())
                .putLong("character", range.getEndCharacter())
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
    return new JsonObjectBuilder("vertex", label);
  }

  private JsonObjectBuilder edge(String label) {
    return new JsonObjectBuilder("edge", label);
  }

  public long emitResultSet() {
    return vertex("resultSet").emit();
  }

  public void emitNext(long outV, long inV) {
    edge("next").putLong("outV", outV).putLong("inV", inV).emit();
  }

  public long emitDefinitionResult() {
    return 0;
  }

  public void emitDefinitionEdge(long outV, long inV) {
    edge("textDocument/definition").putLong("outV", outV).putLong("inV", inV).emit();
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
    edge("textDocument/hover").putLong("outV", outV).putLong("inV", inV).emit();
  }

  public void emitItem(long outV, long inV, long document) {
    edge("item")
        .putLong("outV", outV)
        .putElement("inVs", jsonArray(new JsonPrimitive(inV)))
        .putLong("document", document)
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
    return new JsonObjectBuilder();
  }

  private class JsonObjectBuilder {
    public final long id;
    public final JsonObject object;

    private JsonObjectBuilder() {
      this.object = new JsonObject();
      this.id = -1;
    }

    private JsonObjectBuilder(String type, String label) {
      this.id = LsifWriter.this.id.getAndIncrement();
      this.object = new JsonObject();
      putLong("id", id);
      putString("type", type);
      putString("label", label);
    }

    public JsonObjectBuilder putString(String key, String value) {
      object.add(key, new JsonPrimitive(value));
      return this;
    }

    public JsonObjectBuilder putElement(String key, JsonElement value) {
      object.add(key, value);
      return this;
    }

    private <T extends Number> JsonObjectBuilder putLong(String key, Number value) {
      object.add(key, new JsonPrimitive(value));
      return this;
    }

    public JsonObject build() {
      return object;
    }

    public long emit() {
      if (id < 0) throw new IllegalStateException(gson.toJson(this));

      output.println(gson.toJson(object));
      return id;
    }
  }
}

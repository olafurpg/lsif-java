package com.sourcegraph.lsif_semanticdb;

import com.google.gson.*;

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
            .putString("language", doc.semanticdb.getLanguage().toString())
            .emit();
    doc.id = docId;
    return docId;
  }

  public <T extends Number> void emitContains(long outV, Iterable<T> inVs) {
    edge("contains").putLong("outV", outV).putElement("inVs", jsonArray(inVs)).emit();
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

  private JsonObjectBuilder vertex(String label) {
    return new JsonObjectBuilder("vertex", label);
  }

  private JsonObjectBuilder edge(String label) {
    return new JsonObjectBuilder("edge", label);
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

    private JsonObjectBuilder putLong(String key, long value) {
      object.add(key, new JsonPrimitive(value));
      return this;
    }

    public JsonObject build() {
      return object;
    }

    public long emit() {
      if (id < 0) throw new IllegalStateException(gson.toJson(this));

      output.println(gson.toJson(this));
      return id;
    }
  }
}

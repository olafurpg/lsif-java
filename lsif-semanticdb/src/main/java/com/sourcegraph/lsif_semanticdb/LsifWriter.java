package com.sourcegraph.lsif_semanticdb;

import com.google.gson.*;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicLong;

public class LsifWriter implements AutoCloseable {
  private final Path tmp;
  private final OutputStream output;
  private final LsifSemanticdbOptions options;
  private final Gson gson;
  private final AtomicLong id;

  public LsifWriter(LsifSemanticdbOptions options) throws IOException {
    this.tmp = Files.createTempFile("lsif-semanticdb", "dump.lsif");
    this.output = new BufferedOutputStream(Files.newOutputStream(tmp));
    this.options = options;
    this.gson = new Gson();
    this.id = new AtomicLong();
  }

  private long emitObject(JsonObject object, String type, String label) {
    object.add("type", new JsonPrimitive(type));
    object.add("label", new JsonPrimitive(label));
    long id = this.id.getAndIncrement();
    object.add("id", new JsonPrimitive(this.id));
    try {
      output.write(gson.toJson(object).getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      options.reporter.error(e);
    }
    return id;
  }

  private long emitVertex(JsonObject object, String label) {
    return emitObject(object, "vertex", label);
  }

  private long emitEdge(JsonObject object, String label) {
    return emitObject(object, "edge", label);
  }

  public void emitMetaData() {
    JsonObject metaData = new JsonObject();
    JsonObject toolInfo = new JsonObject();
    metaData.add("version", new JsonPrimitive("0.4.3"));
    metaData.add("projectRoot", new JsonPrimitive(options.sourceroot.toUri().toString()));
    metaData.add("positionEncoding", new JsonPrimitive("utf-16"));
    metaData.add("toolInfo", toolInfo);
    toolInfo.add("name", new JsonPrimitive(options.toolInfo.name));
    toolInfo.add("version", new JsonPrimitive(options.toolInfo.version));
    JsonArray args = new JsonArray();
    for (String arg : options.toolInfo.args) {
      args.add(new JsonPrimitive(arg));
    }
    toolInfo.add("args", args);
    emitVertex(metaData, "metaData");
  }

  public long emitProject(String language) {
    JsonObject project = new JsonObject();
    project.add("uri", new JsonPrimitive(language));
    return emitVertex(project, "project");
  }

  public long emitDocument(LsifDocument doc) {
    JsonObject project = new JsonObject();
    project.add("uri", new JsonPrimitive(doc.semanticdb.getUri()));
    project.add("language", new JsonPrimitive(doc.semanticdb.getLanguage().toString()));
    long id = emitVertex(project, "document");
    doc.id = id;
    return id;
  }

  public long emitContains(long outV, Iterable<Long> inV) {
    JsonObject project = new JsonObject();
    JsonArray ids = new JsonArray();
    for (Long in : inV) {
      ids.add(new JsonPrimitive(in));
    }
    return emitEdge(project, "contains");
  }

  public void build() throws IOException {
    close();
    Files.copy(tmp, options.output, StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public void close() throws IOException {
    output.flush();
    output.close();
  }
}

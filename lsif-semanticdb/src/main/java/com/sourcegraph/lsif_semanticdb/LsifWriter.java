package com.sourcegraph.lsif_semanticdb;

import com.google.gson.*;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Config;
import com.jsoniter.spi.JsoniterSpi;
import com.sourcegraph.lsif_protocol.LsifCodegenConfig;
import com.sourcegraph.lsif_protocol.LsifContainsEdge;
import com.sourcegraph.lsif_protocol.LsifDocument;
import com.sourcegraph.lsif_protocol.LsifHoverResult;
import com.sourcegraph.lsif_protocol.LsifHoverResult.Content;
import com.sourcegraph.lsif_protocol.LsifHoverResult.Result;
import com.sourcegraph.lsif_protocol.LsifItemEdge;
import com.sourcegraph.lsif_protocol.LsifMetaData;
import com.sourcegraph.lsif_protocol.LsifEdge;
import com.sourcegraph.lsif_protocol.LsifNode;
import com.sourcegraph.lsif_protocol.LsifObject;
import com.sourcegraph.lsif_protocol.LsifPosition;
import com.sourcegraph.lsif_protocol.LsifProject;
import com.sourcegraph.lsif_protocol.LsifRange;
import com.sourcegraph.semanticdb_javac.Semanticdb;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

public class LsifWriter implements AutoCloseable {

  private final ThreadLocal<ByteArrayOutputStream> baos =
      ThreadLocal.withInitial(ByteArrayOutputStream::new);
  private final Path tmp;
  private final LsifOutputStream output;
  private final LsifSemanticdbOptions options;
  private final AtomicLong id;
  private final Gson gson;
  private final Config jsoniterConfig;

  public LsifWriter(LsifSemanticdbOptions options) throws IOException {
    this.tmp = Files.createTempFile("lsif-semanticdb", "dump.lsif");
    this.output =
        new LsifOutputStream(
            new PrintStream(new BufferedOutputStream(Files.newOutputStream(tmp)), false, "utf8"));
    this.options = options;
    this.id = new AtomicLong();
    this.gson = new Gson();
    new LsifCodegenConfig().setup();
    jsoniterConfig = JsoniterSpi.getCurrentConfig();
  }

  public void emitMetaData() {
    emitObject(
        new LsifMetaData(
            nextId(), "0.4.3", options.sourceroot.toUri().toString(), options.toolInfo));
  }

  public long emitProject(String language) {
    long id = nextId();
    emitObject(new LsifProject(id, language));
    return id;
  }

  public long emitDocument(ParsedTextDocument doc) {
    long id = nextId();
    emitObject(
        new LsifDocument(
            id, doc.semanticdb.getUri(), doc.semanticdb.getLanguage().toString().toLowerCase()));
    return id;
  }

  public void emitContains(long outV, List<Long> inVs) {
    long[] longs = new long[inVs.size()];
    for (int i = 0; i < inVs.size(); i++) {
      longs[i] = inVs.get(i);
    }
    emitObject(new LsifContainsEdge(nextId(), outV, longs));
  }

  public long emitRange(Semanticdb.Range range) {
    long id = nextId();
    emitObject(
        new LsifRange(
            id,
            new LsifPosition(range.getStartLine(), range.getStartCharacter()),
            new LsifPosition(range.getEndLine(), range.getEndCharacter())));
    return id;
  }

  public long emitResultSet() {
    long id = nextId();
    emitObject(new LsifNode(id, "vertex", "resultSet"));
    return id;
  }

  public void emitNext(long outV, long inV) {
    emitObject(new LsifEdge(nextId(), "next", outV, inV));
  }

  public LsifNode emitReferenceResult() {
    return new LsifNode(nextId(), "vertex", "referenceResult");
  }

  public LsifNode emitDefinitionResult() {
    return new LsifNode(nextId(), "vertex", "definitionResult");
  }

  public void emitDefinitionEdge(long outV, long inV) {
    emitObject(new LsifEdge(nextId(), "textDocument/definition", outV, inV));
  }

  public void emitReferenceEdge(long outV, long inV) {
    emitObject(new LsifEdge(nextId(), "textDocument/reference", outV, inV));
  }

  public void emitHoverEdge(long outV, long inV) {
    emitObject(new LsifEdge(nextId(), "textDocument/hover", outV, inV));
  }

  public long emitHoverResult(Semanticdb.Language language, String value) {
    long id = nextId();
    emitObject(
        new LsifHoverResult(
            id,
            new Result(
                Collections.singletonList(
                    new Content(language.toString().toLowerCase(Locale.ROOT), value)))));

    return id;
  }

  public void emitItem(long outV, long inV, long document) {
    LsifItemEdge item = new LsifItemEdge(nextId(), outV, inV, document);
    emitObject(item);
  }

  public void build() throws IOException {
    close();
    Files.copy(tmp, options.output, StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public void close() throws IOException {
    output.flush();
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

  public void flush() {
    try {
      output.flush();
    } catch (IOException e) {
      options.reporter.error(e);
    }
  }

  public void emitObject(LsifObject object) {
    ByteArrayOutputStream bytes = baos.get();
    bytes.reset();
    JsonStream.serialize(jsoniterConfig, object, bytes);
    output.write(bytes.toByteArray());
  }
}

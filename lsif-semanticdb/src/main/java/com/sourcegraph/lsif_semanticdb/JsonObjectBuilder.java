package com.sourcegraph.lsif_semanticdb;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class JsonObjectBuilder {

  public final long id;
  public final JsonObject object = new JsonObject();
  public final Gson gson;
  public final LsifOutputStream output;
  public final AtomicBoolean isEmitted;

  public JsonObjectBuilder(Gson gson, LsifOutputStream output) {
    this.gson = gson;
    this.output = output;
    this.id = -1;
    isEmitted = new AtomicBoolean(false);
  }

  public JsonObjectBuilder(long id, String type, String label, Gson gson, LsifOutputStream output) {
    this.gson = gson;
    this.output = output;
    this.id = id;
    putNumber("id", id);
    putString("type", type);
    putString("label", label);
    isEmitted = new AtomicBoolean(false);
  }

  public JsonObjectBuilder putString(String key, String value) {
    object.add(key, new JsonPrimitive(value));
    return this;
  }

  public JsonObjectBuilder putElement(String key, JsonElement value) {
    object.add(key, value);
    return this;
  }

  public <T extends Number> JsonObjectBuilder putOutIn(T outV, Number inV) {
    putNumber("outV", outV);
    putNumber("inV", inV);
    return this;
  }

  public <T extends Number> JsonObjectBuilder putNumber(String key, T value) {
    object.add(key, new JsonPrimitive(value));
    return this;
  }

  public JsonObject build() {
    return object;
  }

  public long emit() {
    emitBoolean();
    return id;
  }

  public boolean emitBoolean() {
    if (id < 0) {
      throw new IllegalStateException(gson.toJson(this));
    }
    if (isEmitted.compareAndSet(false, true)) {
      //      byte[] bytes = gson.toJson(object).getBytes(StandardCharsets.UTF_8);
      byte[] bytes =
          "asdwfasdfasdklfjasdkfljasdlkfjasdlkasdflakjsdflkajsdfklj"
              .getBytes(StandardCharsets.UTF_8);
      output.write(bytes);
      return true;
    } else {
      return false;
    }
  }
}

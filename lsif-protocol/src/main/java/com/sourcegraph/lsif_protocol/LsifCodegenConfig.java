package com.sourcegraph.lsif_protocol;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import com.jsoniter.spi.TypeLiteral;
import com.jsoniter.static_codegen.StaticCodegenConfig;
import java.util.List;
import java.util.Map;

public class LsifCodegenConfig implements StaticCodegenConfig {

  @Override
  public void setup() {
    Any.registerEncoders();
    JsonIterator.setMode(DecodingMode.STATIC_MODE);
    JsonStream.setMode(EncodingMode.STATIC_MODE);
    JsonStream.setIndentionStep(2);
  }

  @Override
  public TypeLiteral[] whatToCodegen() {
    return new TypeLiteral[] {
      // generic types, need to use this syntax
      new TypeLiteral<List<Integer>>() {},
      new TypeLiteral<List<Project>>() {},
      new TypeLiteral<Map<String, Object>>() {},
      // array
      TypeLiteral.create(int[].class),
      // object
      TypeLiteral.create(Project.class)
    };
  }
}

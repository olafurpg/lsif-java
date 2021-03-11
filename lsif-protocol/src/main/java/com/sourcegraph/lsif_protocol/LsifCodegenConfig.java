package com.sourcegraph.lsif_protocol;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import com.jsoniter.spi.TypeLiteral;
import com.jsoniter.static_codegen.StaticCodegenConfig;
import java.util.List;

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
      new TypeLiteral<List<Long>>() {},
      new TypeLiteral<List<LsifHoverResult.Content>>() {},
      // array
      TypeLiteral.create(String[].class),
      TypeLiteral.create(Long.class),
      // object
      TypeLiteral.create(LsifProject.class),
      TypeLiteral.create(LsifToolInfo.class),
      TypeLiteral.create(LsifMetaData.class),
      TypeLiteral.create(LsifContainsEdge.class),
      TypeLiteral.create(LsifMultiEdge.class),
      TypeLiteral.create(LsifHoverResult.class),
      TypeLiteral.create(LsifHoverResult.Content.class),
      TypeLiteral.create(LsifHoverResult.Result.class),
    };
  }
}

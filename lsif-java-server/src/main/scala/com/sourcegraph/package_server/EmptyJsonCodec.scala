package com.sourcegraph.package_server

import moped.json.JsonCodec
import moped.json.{DecodingContext, Result}
import moped.json.JsonElement
import moped.macros.ClassShape
import moped.json.JsonString
import moped.json.ErrorResult
import moped.reporters.Diagnostic

class EmptyJsonCodec[T] extends JsonCodec[T] {
  def decode(context: DecodingContext): Result[T] =
    ErrorResult(Diagnostic.error(s"not supported: $context"))
  def encode(value: T): JsonElement = JsonString(value.toString())
  def shape: ClassShape = ClassShape.empty
}

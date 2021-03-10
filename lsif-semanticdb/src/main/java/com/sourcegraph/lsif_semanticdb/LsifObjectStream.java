package com.sourcegraph.lsif_semanticdb;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class LsifObjectStream {
  private final OutputStream out;
  private final ConcurrentLinkedDeque<byte[]> buffer;
  private final AtomicBoolean isFlushing;
  private static final byte[] NEWLINE = "\n".getBytes(StandardCharsets.UTF_8);

  public LsifObjectStream(OutputStream out) {
    this.out = out;
    buffer = new ConcurrentLinkedDeque<>();
    isFlushing = new AtomicBoolean(false);
  }

  public void write(byte[] bytes) {
    buffer.add(bytes);
  }

  public void flush() throws IOException {
    if (isFlushing.compareAndSet(false, true)) {
      byte[] bytes = buffer.poll();
      while (bytes != null) {
        out.write(bytes);
        out.write(NEWLINE);
        bytes = buffer.poll();
      }
      out.flush();
      isFlushing.set(false);
    }
  }
}

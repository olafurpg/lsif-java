package com.sourcegraph.lsif_semanticdb;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class LsifWriter implements AutoCloseable {
    private final Path tmp;
    private final OutputStream output;
    private final LsifSemanticdbOptions options;
    private final Gson gson;

    public LsifWriter(LsifSemanticdbOptions options) throws IOException {
        this.tmp = Files.createTempFile("lsif-semanticdb", "dump.lsif");
        this.output = new BufferedOutputStream(Files.newOutputStream(tmp));;
        this.options = options;
        this.gson = new Gson();
    }

    private void emitObject(JsonObject object) {
        try {
            output.write(gson.toJson(object).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            options.reporter.error(e);
        }
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

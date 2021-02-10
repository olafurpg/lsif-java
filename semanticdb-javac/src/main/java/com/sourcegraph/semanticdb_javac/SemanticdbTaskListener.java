package com.sourcegraph.semanticdb_javac;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import scala.meta.internal.semanticdb.Semanticdb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SemanticdbTaskListener implements TaskListener {
    private final SemanticdbOptions options;
    private final JavacTask task;
    private final SymbolsCache globals;
    private final SemanticdbReporter reporter;

    public SemanticdbTaskListener(SemanticdbOptions options, JavacTask task, SymbolsCache globals, SemanticdbReporter reporter) {
        this.options = options;
        this.task = task;
        this.globals = globals;
        this.reporter = reporter;
    }

    @Override
    public void started(TaskEvent e) {

    }

    @Override
    public void finished(TaskEvent e) {
        if (e.getKind() != TaskEvent.Kind.ANALYZE) return;
        try {
            onFinishedAnalyze(e);
        } catch (Exception ex) {
            // Catch exceptions because we don't want to stop the compilation even if this plugin has a bug.
            // We report the full stack trace to the user because uncaught exceptions should only happen
            // in *exceptional* situations and they should be reported upstream.
            reporter.exception(ex);
        }
    }

    private void onFinishedAnalyze(TaskEvent e) {
        Result<Path, String> path = semanticdbOutputPath(options, e);
        if (path.isOk()) {
            Semanticdb.TextDocument textDocument =
                    new SemanticdbVisitor(task, globals, e, options).buildTextDocument(e.getCompilationUnit());
            writeSemanticdb(path.getOrThrow(), textDocument);
        } else {
            reporter.error(path.getErrorOrThrow());
        }

    }


    private void writeSemanticdb(Path output, Semanticdb.TextDocument textDocument) {
        try {
            byte[] bytes = Semanticdb.TextDocuments.newBuilder().addDocuments(textDocument).build().toByteArray();
            Files.write(output, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Result<Path, String> semanticdbOutputPath(SemanticdbOptions options, TaskEvent e) {
        Path absolutePath = Paths.get(e.getSourceFile().toUri());
        if (absolutePath.startsWith(options.sourceroot)) {
            Path relativePath = options.sourceroot.relativize(absolutePath);
            String filename = relativePath.getFileName().toString() + ".semanticdb";
            Path semanticdbOutputPath = options.targetroot
                    .resolve("META-INF")
                    .resolve("semanticdb")
                    .resolve(relativePath)
                    .resolveSibling(filename);
            try {
                Files.createDirectories(semanticdbOutputPath.getParent());
                return Result.ok(semanticdbOutputPath);
            } catch (IOException exception) {
                return Result.error(String.format(
                        "failed to create parent directory for '%s'. Error message: %s",
                        semanticdbOutputPath, exception.getMessage()));
            }
        } else {
            return Result.error(String.format(
                    "sourceroot '%s does not contain path '%s'. To fix this problem, update the -sourceroot flag to " +
                            "be a parent directory of this source file.",
                    options.sourceroot, absolutePath));
        }

    }
}

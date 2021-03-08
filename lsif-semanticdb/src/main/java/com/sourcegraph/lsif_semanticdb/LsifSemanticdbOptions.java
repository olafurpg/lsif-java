package com.sourcegraph.lsif_semanticdb;

import java.nio.file.Path;
import java.util.List;

public class LsifSemanticdbOptions {

    public final List<Path> targetroots;
    public final Path output;
    public final Path sourceroot;
    public final LsifSemanticdbReporter reporter;


    public LsifSemanticdbOptions(List<Path> targetroots, Path output, Path sourceroot, LsifSemanticdbReporter reporter) {
        this.targetroots = targetroots;
        this.output = output;
        this.sourceroot = sourceroot;
        this.reporter = reporter;
    }
}

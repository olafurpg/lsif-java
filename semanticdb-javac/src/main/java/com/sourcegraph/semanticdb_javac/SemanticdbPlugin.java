package com.sourcegraph.semanticdb_javac;

import com.sun.source.util.*;

public class SemanticdbPlugin implements Plugin {


    @Override
    public String getName() {
        return "semanticdb-javac";
    }

    @Override
    public void init(JavacTask task, String... args) {
        SemanticdbReporter reporter = new SemanticdbReporter();
        SemanticdbOptions options = SemanticdbOptions.parse(args);
        SymbolsCache globals = new SymbolsCache(options, reporter);
        if (!options.errors.isEmpty()) {
            for (String error : options.errors) {
                reporter.error(error);
            }
        } else {
            task.addTaskListener(new SemanticdbTaskListener(options, task, globals, reporter));
        }
    }


}

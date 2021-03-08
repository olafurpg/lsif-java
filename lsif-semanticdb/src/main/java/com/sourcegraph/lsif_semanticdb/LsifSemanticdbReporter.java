package com.sourcegraph.lsif_semanticdb;

abstract class LsifSemanticdbReporter {
    public void error(Throwable e) {
    }

    public boolean hasErrors() {
        return false;
    }
}

package com.sourcegraph.semanticdb_javac;

import com.sun.tools.javac.code.Symbol;

import java.util.IdentityHashMap;

public class LocalSymbolsCache {
    private final IdentityHashMap<Symbol, String> symbols = new IdentityHashMap<>();
    private int localsCounter = -1;
    public String get(Symbol sym) {
        return symbols.get(sym);
    }
    public String put(Symbol sym) {
        localsCounter++;
        return Symbols.local(String.valueOf(localsCounter));
    }
}

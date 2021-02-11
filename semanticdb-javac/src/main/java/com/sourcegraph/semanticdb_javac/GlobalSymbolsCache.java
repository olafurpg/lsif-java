package com.sourcegraph.semanticdb_javac;

import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;

import static com.sourcegraph.semanticdb_javac.Debugging.pprint;

/** Cache of SemanticDB symbols that can be referenced between files. */
public final class GlobalSymbolsCache {

  private final IdentityHashMap<Symbol, String> globals = new IdentityHashMap<>();
  private final SemanticdbOptions options;

  public GlobalSymbolsCache(SemanticdbOptions options) {
    this.options = options;
  }

  public String semanticdbSymbol(Symbol sym, LocalSymbolsCache locals) {
    String result = globals.get(sym);
    if (result != null) return result;
    String localResult = locals.get(sym);
    if (localResult != null) return localResult;
    result = uncachedSemanticdbSymbol(sym, locals);
    if (SemanticdbSymbols.isGlobal(result)) {
      globals.put(sym, result);
    }
    return result;
  }

  public boolean isNone(Symbol sym) {
    return sym == null || sym.kind == Kinds.NIL || (sym.kind & Kinds.ERRONEOUS) != 0;
  }

  private String uncachedSemanticdbSymbol(Symbol sym, LocalSymbolsCache locals) {
    if (isNone(sym)) return SemanticdbSymbols.NONE;
    String owner = semanticdbSymbol(sym.owner, locals);
    if (owner.equals(SemanticdbSymbols.NONE)) {
      return SemanticdbSymbols.ROOT_PACKAGE;
    } else if (sym instanceof Symbol.VarSymbol && sym.isLocal()) {
      return locals.put(sym);
    }
    SemanticdbSymbols.Descriptor desc = semanticdbDescriptor(sym);
    if (options.verboseEnabled && desc.kind == SemanticdbSymbols.Descriptor.Kind.None) {
      pprint(sym.name.toString());
      pprint(sym.kind);
      pprint(
          String.format(
              "sym: %s (%s - superclass %s)", sym, sym.getClass(), sym.getClass().getSuperclass()));
    }
    return SemanticdbSymbols.global(owner, desc);
  }

  private SemanticdbSymbols.Descriptor semanticdbDescriptor(Symbol sym) {
    if (sym instanceof Symbol.ClassSymbol) {
      return new SemanticdbSymbols.Descriptor(
          SemanticdbSymbols.Descriptor.Kind.Type, sym.name.toString());
    } else if (sym instanceof Symbol.MethodSymbol) {
      return new SemanticdbSymbols.Descriptor(
          SemanticdbSymbols.Descriptor.Kind.Method,
          sym.name.toString(),
          methodDisambiguator((Symbol.MethodSymbol) sym));
    } else if (sym instanceof Symbol.PackageSymbol) {
      return new SemanticdbSymbols.Descriptor(
          SemanticdbSymbols.Descriptor.Kind.Package, sym.name.toString());
    } else if (sym instanceof Symbol.TypeVariableSymbol) {
      return new SemanticdbSymbols.Descriptor(
          SemanticdbSymbols.Descriptor.Kind.TypeParameter, sym.name.toString());
    } else if (sym instanceof Symbol.VarSymbol) {
      return new SemanticdbSymbols.Descriptor(
          SemanticdbSymbols.Descriptor.Kind.Term, sym.name.toString());
    } else {
      return SemanticdbSymbols.Descriptor.NONE;
    }
  }

  /**
   * Computes the method "disambiguator" according to the SemanticDB spec.
   *
   * <p><quote> Concatenation of a left parenthesis ("("), a tag and a right parenthesis (")"). If
   * the definition is not overloaded, the tag is empty. If the definition is overloaded, the tag is
   * computed depending on where the definition appears in the following order:
   *
   * <ul>
   *   <li>non-static overloads first, following the same order as they appear in the original
   *       source,
   *   <li>static overloads secondly, following the same order as they appear in the original source
   * </ul>
   *
   * </quote>
   *
   * <p><a href="https://scalameta.org/docs/semanticdb/specification.html#symbol-2">Link to
   * SemanticDB spec</a>.
   */
  private String methodDisambiguator(Symbol.MethodSymbol sym) {
    Scope.Entry lookup =
        sym.owner.members().lookup(sym.name, s -> s instanceof Symbol.MethodSymbol);
    ArrayList<Symbol> peers = new ArrayList<>();
    while (lookup != null) {
      if (lookup.sym != null) {
        peers.add(lookup.sym);
      }
      lookup = lookup.next();
    }
    // NOTE(olafur): reverse the iteration from `Scope.Entry` to get order in which the symbols are
    // defined in source.
    Collections.reverse(peers);
    // NOTE(olafur): sort static methods last, according to the spec. Historical note: this
    // requirement is
    // part of the SemanticDB spec because static methods and non-static methods have a different
    // "owner" symbol.
    // There is no way to recover the definition order for a mix of static nnon-static method
    // definitions.
    // In practice, it's unusual to mix static and non-static methods so this shouldn't be a big
    // issue.
    peers.sort((a, b) -> Boolean.compare(a.isStatic(), b.isStatic()));
    int index = peers.indexOf(sym);
    if (index == 0) return "()";
    return String.format("(+%d)", index);
  }
}

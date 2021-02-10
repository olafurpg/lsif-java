package com.sourcegraph.semanticdb_javac;

import com.sun.source.tree.*;
import com.sun.source.util.DocTrees;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TreePathScanner;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.EndPosTable;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Position;
import scala.meta.internal.semanticdb.Semanticdb;
import scala.meta.internal.semanticdb.Semanticdb.SymbolOccurrence.Role;

import javax.lang.model.util.Elements;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public class SemanticdbVisitor extends TreePathScanner<Void, Void> {
    private final DocTrees docs;
    private final SymbolsCache globals;
    private final JavacTask task;
    private final TaskEvent event;
    private final SemanticdbOptions options;
    private EndPosTable endPosTable = new EmptyEndPosTable();
    private final LocalSymbolsCache locals = new LocalSymbolsCache();
    private final ArrayList<Semanticdb.SymbolOccurrence> occurrences;
    private final ArrayList<Semanticdb.SymbolInformation> infos;


    public SemanticdbVisitor(JavacTask task, SymbolsCache globals, TaskEvent event, SemanticdbOptions options) {
        this.task = task;
        this.docs = DocTrees.instance(task);
        this.globals = globals;
        this.event = event;
        this.options = options;
        if (event.getCompilationUnit() instanceof JCTree.JCCompilationUnit) {
            this.endPosTable = ((JCTree.JCCompilationUnit) event.getCompilationUnit()).endPositions;
        }
        occurrences = new ArrayList<>();
        infos = new ArrayList<>();
    }

    public Semanticdb.TextDocument buildTextDocument(CompilationUnitTree tree) {
        this.scan(tree, null);
        return Semanticdb.TextDocument
                .newBuilder()
                .setSchema(Semanticdb.Schema.SEMANTICDB4)
                .setLanguage(Semanticdb.Language.JAVA)
                .setUri(semanticdbUri())
                .setText(semanticdbText())
                .setMd5(semanticdbMd5())
                .addAllOccurrences(occurrences)
                .addAllSymbols(infos)
                .build();
    }

    private void emitOccurrence(Symbol sym, JCDiagnostic.DiagnosticPosition pos, Role role) {
        emitOccurrence(sym, pos, role, PositionKind.FROM_START);
    }

    private void emitOccurrence(Symbol sym, JCDiagnostic.DiagnosticPosition pos, Role role, PositionKind kind) {
        Optional<Semanticdb.SymbolOccurrence> occ = semanticdbOccurrence(sym, pos, kind, role);
        occ.ifPresent(occurrences::add);
        if (role == Role.DEFINITION) {
            emitInformation(sym);
        }
    }

    private void emitInformation(Symbol sym) {
        Semanticdb.SymbolInformation info = Semanticdb.SymbolInformation.newBuilder()
                .setSymbol(semanticdbSymbol(sym))
                .setDocumentation(semanticdbDocumentation(sym))
                .build();
        if (Symbols.isGlobal(info.getSymbol())) {
            infos.add(info);
        }
    }


    @Override
    public Void visitClass(ClassTree node, Void unused) {
        if (node instanceof JCTree.JCClassDecl) {
            JCTree.JCClassDecl cls = (JCTree.JCClassDecl) node;
            emitOccurrence(cls.sym, cls, Role.DEFINITION, PositionKind.FROM_POINT);
        }
        return super.visitClass(node, unused);
    }

    @Override
    public Void visitMethod(MethodTree node, Void unused) {
        if (node instanceof JCTree.JCMethodDecl) {
            JCTree.JCMethodDecl meth = (JCTree.JCMethodDecl) node;
            emitOccurrence(meth.sym, meth, Role.DEFINITION, PositionKind.FROM_POINT);
        }
        return super.visitMethod(node, unused);
    }

    @Override
    public Void visitVariable(VariableTree node, Void unused) {
        if (node instanceof JCTree.JCVariableDecl) {
            JCTree.JCVariableDecl decl = (JCTree.JCVariableDecl) node;
            emitOccurrence(decl.sym, decl, Role.DEFINITION, PositionKind.FROM_POINT);
        }
        return super.visitVariable(node, unused);
    }

    @Override
    public Void visitIdentifier(IdentifierTree node, Void unused) {
        if (node instanceof JCTree.JCIdent) {
            JCTree.JCIdent ident = (JCTree.JCIdent) node;
            emitOccurrence(ident.sym, ident, Role.REFERENCE);
        }
        return super.visitIdentifier(node, unused);
    }

    @Override
    public Void visitMemberReference(MemberReferenceTree node, Void unused) {
        if (node instanceof JCTree.JCMemberReference) {
            JCTree.JCMemberReference ref = (JCTree.JCMemberReference) node;
            emitOccurrence(ref.sym, ref, Role.REFERENCE);
        }
        return super.visitMemberReference(node, unused);
    }

    @Override
    public Void visitMemberSelect(MemberSelectTree node, Void unused) {
        if (node instanceof JCTree.JCFieldAccess) {
            JCTree.JCFieldAccess select = (JCTree.JCFieldAccess) node;
            emitOccurrence(select.sym, select, Role.REFERENCE, PositionKind.FROM_POINT_PLUS_ONE);
        }
        return super.visitMemberSelect(node, unused);
    }

    @Override
    public Void visitNewClass(NewClassTree node, Void unused) {
        if (node instanceof JCTree.JCNewClass) {
            JCTree.JCNewClass cls = (JCTree.JCNewClass) node;
            emitOccurrence(cls.constructor, cls, Role.REFERENCE);
        }
        return super.visitNewClass(node, unused);
    }


    private String semanticdbSymbol(Symbol sym) {
        return globals.semanticdbSymbol(sym, locals);
    }

    private Optional<Semanticdb.Range> semanticdbRange(JCDiagnostic.DiagnosticPosition pos, PositionKind kind, Symbol sym) {
        int start, end;
        if (kind.isFromPoint() && sym != null && sym.name != null) {
            start = pos.getPreferredPosition();
            if (kind == PositionKind.FROM_POINT_PLUS_ONE) {
                start++;
            }
            end = start + sym.name.length();
        } else {
            start = pos.getStartPosition();
            end = pos.getEndPosition(endPosTable);
        }
        if (start != Position.NOPOS &&
                end != Position.NOPOS &&
                end > start) {
            LineMap lineMap = event.getCompilationUnit().getLineMap();
            Semanticdb.Range range = Semanticdb.Range.newBuilder()
                    .setStartLine((int) lineMap.getLineNumber(start) - 1)
                    .setStartCharacter((int) lineMap.getColumnNumber(start) - 1)
                    .setEndLine((int) lineMap.getLineNumber(end) - 1)
                    .setEndCharacter((int) lineMap.getColumnNumber(end) - 1)
                    .build();
            return Optional.of(range);
        } else {
            return Optional.empty();
        }
    }

    private Optional<Semanticdb.SymbolOccurrence> semanticdbOccurrence(
            Symbol sym, JCDiagnostic.DiagnosticPosition pos, PositionKind kind, Role role) {
        Optional<Semanticdb.Range> range = semanticdbRange(pos, kind, sym);
        if (range.isPresent()) {
            String ssym = semanticdbSymbol(sym);
            if (!ssym.equals(Symbols.NONE)) {
                Semanticdb.SymbolOccurrence occ = Semanticdb.SymbolOccurrence.newBuilder()
                        .setSymbol(ssym)
                        .setRange(range.get())
                        .setRole(role)
                        .build();
                return Optional.of(occ);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private String semanticdbText() {
        if (!options.includeText) return "";
        try {
            return event.getSourceFile().getCharContent(true).toString();
        } catch (IOException e) {
            return "";
        }
    }

    private String semanticdbMd5() {
        try {
            return MD5.digest(event.getSourceFile().getCharContent(true).toString());
        } catch (IOException | NoSuchAlgorithmException e) {
            return "";
        }
    }

    private String semanticdbUri() {
        Path absolutePath = Paths.get(event.getSourceFile().toUri());
        Path relativePath = options.sourceroot.relativize(absolutePath);
        StringBuilder out = new StringBuilder();
        Iterator<Path> it = relativePath.iterator();
        if (it.hasNext()) out.append(it.next().getFileName().toString());
        while (it.hasNext()) {
            Path part = it.next();
            out.append('/').append(part.getFileName().toString());
        }
        return out.toString();
    }

    private String semanticdbDocumentation(Symbol sym) {
        try {
            Elements elements = task.getElements();
            if (elements == null) return "";

            String doc = elements.getDocComment(sym);
            if (doc == null) return "";

            return doc;
        } catch (NullPointerException e) {
            // Can happen in `getDocComment()`
            // Caused by: java.lang.NullPointerException
            //   at com.sun.tools.javac.model.JavacElements.cast(JavacElements.java:605)
            //   at com.sun.tools.javac.model.JavacElements.getTreeAndTopLevel(JavacElements.java:543)
            //   at com.sun.tools.javac.model.JavacElements.getDocComment(JavacElements.java:321)
            //   at com.sourcegraph.semanticdb_javac.SemanticdbVisitor.semanticdbDocumentation(SemanticdbVisitor.java:233)
            return "";
        }
    }
}

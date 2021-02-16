package com.sourcegraph.semanticdb_javac;

import javassist.*;

import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Arrays;

class JavaCompilerAssistTransformer implements ClassFileTransformer {
  private final PrintStream out;

  private static final String TARGET_CLASS = "com/sun/tools/javac/api/JavacTool";

  private static final String TARGET_METHOD =
      "(Ljava/io/Writer;Ljavax/tools/JavaFileManager;Ljavax/tools/DiagnosticListener;Ljava/lang/Iterable;Ljava/lang/Iterable;Ljava/lang/Iterable;)Ljavax/tools/JavaCompiler$CompilationTask;";

  public JavaCompilerAssistTransformer(PrintStream out) {
    this.out = out;
  }

  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer) {
    if (TARGET_CLASS.equals(className)) {
      out.println("transform: " + className);
      out.println("loader: " + loader);
      out.println("classBeingRedefined: " + classBeingRedefined);
      out.println("protectionDomain: " + protectionDomain);
      ClassPool cp = ClassPool.getDefault();
      try {
        CtClass cc = cp.get(className.replace('/', '.'));
        out.println("cc: " + cc);
        CtMethod[] getTasks = cc.getDeclaredMethods("getTask");
        out.println("methods: " + Arrays.toString(getTasks));
        for (CtMethod method : getTasks) {
          if (TARGET_METHOD.equals(method.getSignature())) {
            method.insertBefore("java.lang.System.out.println(\"Hello from agent!: \" + $4);");
            //            method.insertBefore("throw new RuntimeException();");
            //            method.insertBefore(
            //                "com.sourcegraph.semanticdb_javac.JavaCompilerTransformer.hello();");
          }
        }
        classfileBuffer = cc.toBytecode();
        cc.detach();
      } catch (Exception e) {
        e.printStackTrace(out);
      }
    }
    return classfileBuffer;
  }

  public static void hello() {
    System.out.println("BOOM");
  }
}

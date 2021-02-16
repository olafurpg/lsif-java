package com.sourcegraph.semanticdb_javac;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.PackageDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class JavaCompilerBuddyTransformer implements ClassFileTransformer {
  PrintStream out;
  //    ByteBuddy
  private static final String TARGET_CLASS =
      "org/gradle/api/internal/tasks/compile/JavaCompilerArgumentsBuilder";
  private static final String TARGET_METHOD =
      "(Ljava/io/Writer;Ljavax/tools/JavaFileManager;Ljavax/tools/DiagnosticListener;Ljava/lang/Iterable;Ljava/lang/Iterable;Ljava/lang/Iterable;)Ljavax/tools/JavaCompiler$CompilationTask;";

  public JavaCompilerBuddyTransformer(PrintStream out) {
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
      ClassPool cp = ClassPool.getDefault();
      //      out.println("loader: " + loader);
      //      out.println("classBeingRedefined: " + classBeingRedefined);
      //      out.println("protectionDomain: " + protectionDomain);
      try {
        CtClass ctClass = cp.get(TARGET_CLASS.replace('/', '.'));
        classfileBuffer =
            new ByteBuddy()
                .rebase(ctClass.toClass())
                .method(ElementMatchers.named("getTask"))
                .intercept(
                    Advice.to(SemanticdbInterceptor.class)) // .andThen(SuperMethodCall.INSTANCE))
                .make()
                .getBytes();
      } catch (Exception e) {
        e.printStackTrace(out);
      }
    }
    return classfileBuffer;
  }
}

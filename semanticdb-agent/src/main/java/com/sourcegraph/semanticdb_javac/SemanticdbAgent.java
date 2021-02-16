package com.sourcegraph.semanticdb_javac;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class SemanticdbAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    new AgentBuilder.Default()
        .type(
            named("org.gradle.api.internal.tasks.compile.JavaCompilerArgumentsBuilder")
                .or(named("tests.GradleOptionsBuilder")))
        .transform(
            new AgentBuilder.Transformer.ForAdvice()
                .advice(named("build"), SemanticdbAdvice.class.getName()))
        .installOn(inst);
  }

  @SuppressWarnings("all")
  public static class SemanticdbAdvice {
    @Advice.OnMethodExit
    public static void executeEnter(
        @Advice.Return(readOnly = false, typing = DYNAMIC) List<String> options) {

      String PROCESSORPATH = System.getProperty("semanticdb.processorpath");
      if (PROCESSORPATH == null) throw new NoSuchElementException("-Dsemanticdb.processorpath");
      String SOURCEROOT = System.getProperty("semanticdb.sourceroot");
      if (SOURCEROOT == null) throw new NoSuchElementException("-Dsemanticdb.sourceroot");
      String TARGETROOT = System.getProperty("semanticdb.targetroot");
      if (TARGETROOT == null) throw new NoSuchElementException("-Dsemanticdb.targetroot");

      boolean isProcessorpathUpdated = false;
      String previousOption = "";

      ArrayList<String> newOptions = new ArrayList<>();
      for (String option : options) {
        switch (previousOption) {
          case "-processorpath":
          case "-processor-path":
          case "-classpath":
          case "-class-path":
            isProcessorpathUpdated = true;
            newOptions.add(PROCESSORPATH + File.pathSeparator + option);
            break;
          default:
            newOptions.add(option);
            break;
        }
        previousOption = option;
      }
      if (!isProcessorpathUpdated) {
        newOptions.add("-classpath");
        newOptions.add(PROCESSORPATH);
      }
      newOptions.add(
          String.format(
              "-Xplugin:semanticdb -sourceroot:%s -targetroot:%s", SOURCEROOT, TARGETROOT));

      String debugPath = System.getProperty("semanticdb.debugpath");
      if (debugPath != null) {
        try (PrintStream fos =
            new PrintStream(
                Files.newOutputStream(
                    Paths.get(debugPath), StandardOpenOption.APPEND, StandardOpenOption.CREATE))) {
          fos.println("Java Home: " + System.getProperty("java.home"));
          fos.println("Old Options: " + options);
          fos.println("New Options: " + newOptions);
        } catch (IOException e) {
        }
      }

      options = newOptions;
    }
  }
}

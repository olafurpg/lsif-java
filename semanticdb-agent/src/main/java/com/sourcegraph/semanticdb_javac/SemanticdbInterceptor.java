package com.sourcegraph.semanticdb_javac;

import net.bytebuddy.asm.Advice;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SemanticdbInterceptor {

  @Advice.OnMethodEnter
  public static void intercept(@Advice.Origin Method method, @Advice.AllArguments Object[] args) {
    try {
      PrintStream out =
          new PrintStream(
              Files.newOutputStream(
                  Paths.get(System.getProperty("user.home")).resolve("javaagent.txt"),
                  StandardOpenOption.APPEND,
                  StandardOpenOption.CREATE));
      out.println("INTERCEPTED ARGS: " + Arrays.toString(args));
      if (args.length == 6) {
        List<String> options = (List<String>) args[3];
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

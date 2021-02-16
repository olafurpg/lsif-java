package minimized;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.util.Collections;

@Annotations(value = "value", format = "format")
public class MinimizedJavaMain {
  public static void main(String[] args) {
    TypeVariables.app(new TypeVariables.CT());
    System.out.println(
        Methods.app(42, "42")
            + Enums.app()
            + Docstrings.app()
            + InnerClasses.app()
            + ForComprehensions.app(42)
            + AnonymousClasses.app(42)
            + Primitives.app()
            + new ParameterizedTypes<Integer, String>().app(42, "42")
            + RawTypes.x.toString()
            + ClassOf.app()
            + SubClasses.app()
            + Fields.app());
    JavaCompiler.CompilationTask task =
        ToolProvider.getSystemJavaCompiler()
            .getTask(null, null, null, Collections.singletonList("-Arandom=42"), null, null);
    System.out.println("task: " + task);
  }
}

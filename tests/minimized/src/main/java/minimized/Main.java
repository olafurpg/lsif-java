package minimized;

@Annotations(value = "value", format = "format")
public class Main {
    public static void main(String[] args) {
        System.out.println(Methods.app(42, "42"));
        System.out.println(Enums.app());
        System.out.println(Docstrings.app());
        System.out.println(InnerClasses.app());
        System.out.println(ForComprehensions.app(42));
        System.out.println(AnonymousClasses.app(42));
        System.out.println(Primitives.app());
        TypeVariables.app(new TypeVariables.CT());
        System.out.println(new ParameterizedTypes<Integer, String>().app(42, "42"));
        System.out.println(RawTypes.x.toString());
        System.out.println(ClassOf.app());
        System.out.println(SubClasses.app());
        System.out.println(Fields.app());
    }
}

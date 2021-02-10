package minimized;

@Annotations(value = "value", format = "format")
 ^^^^^^^^^^^ reference minimized/Annotations#
//           ^^^^^ reference minimized/Annotations#value().
//                            ^^^^^^ reference minimized/Annotations#format().
public class Main {
//     ^^^^ definition minimized/Main#
//     ^^^^^^ definition minimized/Main#`<init>`().
    public static void main(String[] args) {
//                     ^^^^ definition minimized/Main#main().
//                          ^^^^^^ reference java/lang/String#
//                                   ^^^^ definition local0
        System.out.println(Methods.app(42, "42"));
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^^ reference minimized/Methods#
//                                 ^^^ reference minimized/Methods#app().
        System.out.println(Enums.app());
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^ reference minimized/Enums#
//                               ^^^ reference minimized/Enums#app().
        System.out.println(Docstrings.app());
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^^^^^ reference minimized/Docstrings#
//                                    ^^^ reference minimized/Docstrings#app().
        System.out.println(InnerClasses.app());
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^^^^^^^ reference minimized/InnerClasses#
//                                      ^^^ reference minimized/InnerClasses#app().
        System.out.println(ForComprehensions.app(42));
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+3).
//                         ^^^^^^^^^^^^^^^^^ reference minimized/ForComprehensions#
//                                           ^^^ reference minimized/ForComprehensions#app().
        System.out.println(AnonymousClasses.app(42));
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+3).
//                         ^^^^^^^^^^^^^^^^ reference minimized/AnonymousClasses#
//                                          ^^^ reference minimized/AnonymousClasses#app().
        System.out.println(Primitives.app());
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^^^^^ reference minimized/Primitives#
//                                    ^^^ reference minimized/Primitives#app().
        TypeVariables.app(new TypeVariables.CT());
//      ^^^^^^^^^^^^^ reference minimized/TypeVariables#
//                    ^^^ reference minimized/TypeVariables#app().
//                        ^^^^^^^^^^^^^^^^^^^^^^ reference minimized/TypeVariables#CT#`<init>`().
//                            ^^^^^^^^^^^^^ reference minimized/TypeVariables#
//                                          ^^ reference minimized/TypeVariables#CT#
        System.out.println(new ParameterizedTypes<Integer, String>().app(42, "42"));
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference minimized/ParameterizedTypes#`<init>`().
//                             ^^^^^^^^^^^^^^^^^^ reference minimized/ParameterizedTypes#
//                                                ^^^^^^^ reference java/lang/Integer#
//                                                         ^^^^^^ reference java/lang/String#
//                                                                   ^^^ reference minimized/ParameterizedTypes#app().
        System.out.println(RawTypes.x.toString());
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^^^ reference minimized/RawTypes#
//                                  ^ reference minimized/RawTypes#x.
//                                    ^^^^^^^^ reference java/lang/Object#toString().
        System.out.println(ClassOf.app());
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^^ reference minimized/ClassOf#
//                                 ^^^ reference minimized/ClassOf#app().
        System.out.println(SubClasses.app());
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^^^^^ reference minimized/SubClasses#
//                                    ^^^ reference minimized/SubClasses#app().
        System.out.println(Fields.app());
//      ^^^^^^ reference java/lang/System#
//             ^^^ reference java/lang/System#out.
//                 ^^^^^^^ reference java/io/PrintStream#println(+8).
//                         ^^^^^^ reference minimized/Fields#
//                                ^^^ reference minimized/Fields#app().
    }
}

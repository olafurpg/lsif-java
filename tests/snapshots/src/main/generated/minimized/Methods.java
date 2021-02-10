package minimized;

public class Methods {
//     ^^^^^^ definition minimized/Methods#`<init>`().
//     ^^^^^^^ definition minimized/Methods#
    private int overload(int value) {
//              ^^^^^^^^ definition minimized/Methods#overload().
//                           ^^^^^ definition local0
        return value + 1;
//             ^^^^^ reference local2
    }
    private String overload(String value) {
//          ^^^^^^ reference java/lang/String#
//                 ^^^^^^^^ definition minimized/Methods#overload(+1).
//                          ^^^^^^ reference java/lang/String#
//                                 ^^^^^ definition local3
        return value + "1";
//             ^^^^^ reference local5
    }

    private static int staticOverload(int value) {
//                     ^^^^^^^^^^^^^^ definition minimized/Methods#staticOverload().
//                                        ^^^^^ definition local6
        return value + 1;
//             ^^^^^ reference local8
    }
    private static String staticOverload(String value) {
//                 ^^^^^^ reference java/lang/String#
//                        ^^^^^^^^^^^^^^ definition minimized/Methods#staticOverload(+1).
//                                       ^^^^^^ reference java/lang/String#
//                                              ^^^^^ definition local9
        return value + "1";
//             ^^^^^ reference local11
    }

    public static String app(int n, String m) {
//                ^^^^^^ reference java/lang/String#
//                       ^^^ definition minimized/Methods#app().
//                               ^ definition local12
//                                  ^^^^^^ reference java/lang/String#
//                                         ^ definition local14
        Methods methods = new Methods();
//      ^^^^^^^ reference minimized/Methods#
//              ^^^^^^^ definition local16
//                        ^^^^^^^^^^^^^ reference minimized/Methods#`<init>`().
//                            ^^^^^^^ reference minimized/Methods#
        int a = staticOverload(n);
//          ^ definition local18
//              ^^^^^^^^^^^^^^ reference minimized/Methods#staticOverload().
//                             ^ reference local20
        String b = staticOverload(m);
//      ^^^^^^ reference java/lang/String#
//             ^ definition local21
//                 ^^^^^^^^^^^^^^ reference minimized/Methods#staticOverload(+1).
//                                ^ reference local23
        int c = methods.overload(n);
//          ^ definition local24
//              ^^^^^^^ reference local26
//                      ^^^^^^^^ reference minimized/Methods#overload().
//                               ^ reference local27
        String d = methods.overload(m);
//      ^^^^^^ reference java/lang/String#
//             ^ definition local28
//                 ^^^^^^^ reference local30
//                         ^^^^^^^^ reference minimized/Methods#overload(+1).
//                                  ^ reference local31
        return b + a + c + d;
//             ^ reference local32
//                 ^ reference local33
//                     ^ reference local34
//                         ^ reference local35
    }
}

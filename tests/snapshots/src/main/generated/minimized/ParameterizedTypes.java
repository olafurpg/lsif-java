package minimized;

public class ParameterizedTypes<A, B> {
//     ^^^^^^ definition minimized/ParameterizedTypes#`<init>`().
//     ^^^^^^^^^^^^^^^^^^ definition minimized/ParameterizedTypes#
    public String app(A a, B b) {
//         ^^^^^^ reference java/lang/String#
//                ^^^ definition minimized/ParameterizedTypes#app().
//                    ^ reference minimized/ParameterizedTypes#[A]
//                      ^ definition local0
//                         ^ reference minimized/ParameterizedTypes#[B]
//                           ^ definition local2
        return a.toString() + b;
//             ^ reference local4
//               ^^^^^^^^ reference java/lang/Object#toString().
//                            ^ reference local5
    }
}

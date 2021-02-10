package minimized;

import java.util.function.Function;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^ reference java/util/function/
//                        ^^^^^^^^ reference java/util/function/Function#

@SuppressWarnings("ALL")
 ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
public class AnonymousClasses {
//     ^^^^^^ definition minimized/AnonymousClasses#`<init>`().
//     ^^^^^^^^^^^^^^^^ definition minimized/AnonymousClasses#
    public static int app(int n) {
//                    ^^^ definition minimized/AnonymousClasses#app().
//                            ^ definition local0
        Function<Integer, Integer> fn = new Function<Integer, Integer>() {
//      ^^^^^^^^ reference java/util/function/Function#
//               ^^^^^^^ reference java/lang/Integer#
//                        ^^^^^^^ reference java/lang/Integer#
//                                 ^^ definition local2
//                                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference minimized/AnonymousClasses#app().``#`<init>`(). 5:9
//                                          ^^^^^^^^ reference java/util/function/Function#
//                                          ^^^^^^^^ reference java/util/function/Function#
//                                                   ^^^^^^^ reference java/lang/Integer#
//                                                   ^^^^^^^ reference java/lang/Integer#
//                                                            ^^^^^^^ reference java/lang/Integer#
//                                                            ^^^^^^^ reference java/lang/Integer#
//                                                                       ^ definition minimized/AnonymousClasses#app().``#`<init>`(). 1:4
            @Override
//           ^^^^^^^^ reference java/lang/Override#
            public Integer apply(Integer integer) {
//                 ^^^^^^^ reference java/lang/Integer#
//                         ^^^^^ definition minimized/AnonymousClasses#app().``#apply().
//                               ^^^^^^^ reference java/lang/Integer#
//                                       ^^^^^^^ definition local4
                return integer + n;
//                     ^^^^^^^ reference local6
//                               ^ reference local7
            }
        };

        return fn.apply(n);
//             ^^ reference local8
//                ^^^^^ reference java/util/function/Function#apply().
//                      ^ reference local9
    }
}

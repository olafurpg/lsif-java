package minimized;

import java.util.Arrays;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^ reference java/util/Arrays#

public enum Enums {
//     ^^^^^ definition minimized/Enums#
    A("A"), B("B"), C("C");
//  ^ definition minimized/Enums#A.
//   ^^^^^ reference minimized/Enums#`<init>`().
//          ^ definition minimized/Enums#B.
//           ^^^^^ reference minimized/Enums#`<init>`().
//                  ^ definition minimized/Enums#C.
//                   ^^^^^ reference minimized/Enums#`<init>`().
    public String value;
//         ^^^^^^ reference java/lang/String#
//                ^^^^^ definition minimized/Enums#value.

    Enums(String value) {
//  ^^^^^^ definition minimized/Enums#`<init>`().
//        ^^^^^^ reference java/lang/String#
//               ^^^^^ definition local0
        this.value = value;
//      ^^^^ reference minimized/Enums#this.
//           ^^^^^ reference minimized/Enums#value.
//                   ^^^^^ reference local2
    }

    public static String app() {
//                ^^^^^^ reference java/lang/String#
//                       ^^^ definition minimized/Enums#app().
        String all = Arrays
//      ^^^^^^ reference java/lang/String#
//             ^^^ definition local3
//                   ^^^^^^ reference java/util/Arrays#
                .stream(values())
//               ^^^^^^ reference java/util/Arrays#stream().
//                      ^^^^^^ reference minimized/Enums#values().
                .map(e -> e.value)
//               ^^^ reference java/util/stream/Stream#map().
//                   ^ definition local5
//                    ^^^^^ reference minimized/Enums#
//                        ^ reference local7
//                          ^^^^^ reference minimized/Enums#value.
                .map(Enums::valueOf)
//               ^^^ reference java/util/stream/Stream#map().
//                   ^^^^^ reference minimized/Enums#
//                   ^^^^^^^^^^^^^^ reference minimized/Enums#valueOf().
                .toString();
//               ^^^^^^^^ reference java/lang/Object#toString().
        return all + A.value + B.value + C.value;
//             ^^^ reference local8
//                   ^ reference minimized/Enums#A.
//                     ^^^^^ reference minimized/Enums#value.
//                             ^ reference minimized/Enums#B.
//                               ^^^^^ reference minimized/Enums#value.
//                                       ^ reference minimized/Enums#C.
//                                         ^^^^^ reference minimized/Enums#value.
    }
}

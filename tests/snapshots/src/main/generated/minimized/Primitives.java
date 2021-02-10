package minimized;

import java.util.Random;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^ reference java/util/Random#

public class Primitives {
//     ^^^^^^ definition minimized/Primitives#`<init>`().
//     ^^^^^^^^^^ definition minimized/Primitives#
    public static String app() {
//                ^^^^^^ reference java/lang/String#
//                       ^^^ definition minimized/Primitives#app().
        Random random = new Random();
//      ^^^^^^ reference java/util/Random#
//             ^^^^^^ definition local0
//                      ^^^^^^^^^^^^ reference java/util/Random#`<init>`().
//                          ^^^^^^ reference java/util/Random#
        byte a = (byte) random.nextInt();
//           ^ definition local2
//                      ^^^^^^ reference local4
//                             ^^^^^^^ reference java/util/Random#nextInt().
        short b = (short) random.nextInt();
//            ^ definition local5
//                        ^^^^^^ reference local7
//                               ^^^^^^^ reference java/util/Random#nextInt().
        int c = random.nextInt();
//          ^ definition local8
//              ^^^^^^ reference local10
//                     ^^^^^^^ reference java/util/Random#nextInt().
        long d = random.nextLong();
//           ^ definition local11
//               ^^^^^^ reference local13
//                      ^^^^^^^^ reference java/util/Random#nextLong().
        char e = (char) random.nextInt();
//           ^ definition local14
//                      ^^^^^^ reference local16
//                             ^^^^^^^ reference java/util/Random#nextInt().
        float f = (float) random.nextDouble();
//            ^ definition local17
//                        ^^^^^^ reference local19
//                               ^^^^^^^^^^ reference java/util/Random#nextDouble().
        double g = (double) random.nextDouble();
//             ^ definition local20
//                          ^^^^^^ reference local22
//                                 ^^^^^^^^^^ reference java/util/Random#nextDouble().
        boolean h = random.nextBoolean();
//              ^ definition local23
//                  ^^^^^^ reference local25
//                         ^^^^^^^^^^^ reference java/util/Random#nextBoolean().
        return "" + a + b + c + d + e + f + g + h;
//                  ^ reference local26
//                      ^ reference local27
//                          ^ reference local28
//                              ^ reference local29
//                                  ^ reference local30
//                                      ^ reference local31
//                                          ^ reference local32
//                                              ^ reference local33
    }
}

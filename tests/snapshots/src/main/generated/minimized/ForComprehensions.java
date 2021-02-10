package minimized;

import java.util.Collections;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^^ reference java/util/Collections#
import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#

public class ForComprehensions {
//     ^^^^^^ definition minimized/ForComprehensions#`<init>`().
//     ^^^^^^^^^^^^^^^^^ definition minimized/ForComprehensions#
    public static int app(int n) {
//                    ^^^ definition minimized/ForComprehensions#app().
//                            ^ definition local0
        List<Integer> integers = Collections.singletonList(n);
//      ^^^^ reference java/util/List#
//           ^^^^^^^ reference java/lang/Integer#
//                    ^^^^^^^^ definition local2
//                               ^^^^^^^^^^^ reference java/util/Collections#
//                                           ^^^^^^^^^^^^^ reference java/util/Collections#singletonList().
//                                                         ^ reference local4
        int result = 0;
//          ^^^^^^ definition local5
        for (int i : integers) {
//               ^ definition local7
//                   ^^^^^^^^ reference local9
            result += i;
//          ^^^^^^ reference local10
//                    ^ reference local11
        }
        return result;
//             ^^^^^^ reference local12
    }
}

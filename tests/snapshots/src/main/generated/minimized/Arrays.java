package minimized;

public class Arrays {
//     ^^^^^^ definition minimized/Arrays#
//     ^^^^^^ definition minimized/Arrays#`<init>`().
    public static String app()  {
//                ^^^^^^ reference java/lang/String#
//                       ^^^ definition minimized/Arrays#app().
        int[] a = {1, 2, 3};
//            ^ definition local0
        String[] b = {"1", "2", "3"};
//      ^^^^^^ reference java/lang/String#
//               ^ definition local2
        int[][] c = {{1}, {2}, {3}};
//              ^ definition local4
        String[][] d = {{"1"}, {"2"}, {"3"}};
//      ^^^^^^ reference java/lang/String#
//                 ^ definition local6
        return b[0] + a[0] + c[1][0] + d[1][0];
//             ^ reference local8
//                    ^ reference local9
//                           ^ reference local10
//                                     ^ reference local11
    }
}

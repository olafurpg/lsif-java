package com.airbnb.epoxy;

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#

/**
 * Utilities for generating 64-bit long IDs from types such as {@link CharSequence}.
 */
public final class IdUtils {
//                 ^^^^^^^ definition com/airbnb/epoxy/IdUtils# public final class IdUtils
  private IdUtils() {
//        ^^^^^^^ definition com/airbnb/epoxy/IdUtils#`<init>`(). private IdUtils()
  }

  /**
   * Hash a long into 64 bits instead of the normal 32. This uses a xor shift implementation to
   * attempt psuedo randomness so object ids have an even spread for less chance of collisions.
   * <p>
   * From http://stackoverflow.com/a/11554034
   * <p>
   * http://www.javamex.com/tutorials/random_numbers/xorshift.shtml
   */
  public static long hashLong64Bit(long value) {
//                   ^^^^^^^^^^^^^ definition com/airbnb/epoxy/IdUtils#hashLong64Bit(). public static long hashLong64Bit(long value)
//                                      ^^^^^ definition local0 long value
    value ^= (value << 21);
//  ^^^^^ reference local0
//            ^^^^^ reference local0
    value ^= (value >>> 35);
//  ^^^^^ reference local0
//            ^^^^^ reference local0
    value ^= (value << 4);
//  ^^^^^ reference local0
//            ^^^^^ reference local0
    return value;
//         ^^^^^ reference local0
  }

  /**
   * Hash a string into 64 bits instead of the normal 32. This allows us to better use strings as a
   * model id with less chance of collisions. This uses the FNV-1a algorithm for a good mix of speed
   * and distribution.
   * <p>
   * Performance comparisons found at http://stackoverflow.com/a/1660613
   * <p>
   * Hash implementation from http://www.isthe.com/chongo/tech/comp/fnv/index.html#FNV-1a
   */
  public static long hashString64Bit(@Nullable CharSequence str) {
//                   ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/IdUtils#hashString64Bit(). public static long hashString64Bit(CharSequence str)
//                                    ^^^^^^^^ reference androidx/annotation/Nullable#
//                                             ^^^^^^^^^^^^ reference java/lang/CharSequence#
//                                                          ^^^ definition local1 @Nullable CharSequence str
    if (str == null) {
//      ^^^ reference local1
      return 0;
    }

    long result = 0xcbf29ce484222325L;
//       ^^^^^^ definition local2 long result
    final int len = str.length();
//            ^^^ definition local3 final int len
//                  ^^^ reference local1
//                      ^^^^^^ reference java/lang/CharSequence#length().
    for (int i = 0; i < len; i++) {
//           ^ definition local4 int i
//                  ^ reference local4
//                      ^^^ reference local3
//                           ^ reference local4
      result ^= str.charAt(i);
//    ^^^^^^ reference local2
//              ^^^ reference local1
//                  ^^^^^^ reference java/lang/CharSequence#charAt().
//                         ^ reference local4
      result *= 0x100000001b3L;
//    ^^^^^^ reference local2
    }
    return result;
//         ^^^^^^ reference local2
  }
}

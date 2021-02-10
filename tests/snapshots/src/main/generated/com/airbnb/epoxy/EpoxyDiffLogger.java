package com.airbnb.epoxy;

import android.util.Log;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/util/
//                  ^^^ reference android/util/Log#

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView/
//                                               ^^^^^^^^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView/AdapterDataObserver#

/**
 * This data observer can be registered with an Epoxy adapter or controller to log all item change
 * events. This may be useful to use in debug builds in order to observe model updates and monitor
 * for issues.
 * <p>
 * You may want to look for unexpected item updates to catch improper hashCode/equals
 * implementations in your models.
 * <p>
 * Additionally, you may want to look for frequent or unnecessary updates as an opportunity for
 * optimization.
 */
public class EpoxyDiffLogger extends AdapterDataObserver {
//     ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyDiffLogger#
//                                   ^^^^^^^^^^^^^^^^^^^ reference _root_/
  private final String tag;
//              ^^^^^^ reference java/lang/String#
//                     ^^^ definition com/airbnb/epoxy/EpoxyDiffLogger#tag.

  public EpoxyDiffLogger(String tag) {
//       ^^^^^^ definition com/airbnb/epoxy/EpoxyDiffLogger#`<init>`().
//                       ^^^^^^ reference java/lang/String#
//                              ^^^ definition local0
    this.tag = tag;
//  ^^^^ reference com/airbnb/epoxy/EpoxyDiffLogger#this.
//       ^^^ reference com/airbnb/epoxy/EpoxyDiffLogger#tag.
//             ^^^ reference local2
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onItemRangeChanged(int positionStart, int itemCount) {
//            ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyDiffLogger#onItemRangeChanged().
//                                   ^^^^^^^^^^^^^ definition local3
//                                                      ^^^^^^^^^ definition local5
    Log.d(tag, "Item range changed. Start: " + positionStart + " Count: " + itemCount);
//  ^^^ reference _root_/
//      ^ reference d#
//        ^^^ reference com/airbnb/epoxy/EpoxyDiffLogger#tag.
//                                             ^^^^^^^^^^^^^ reference local7
//                                                                          ^^^^^^^^^ reference local8
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
//            ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyDiffLogger#onItemRangeChanged(+1).
//                                   ^^^^^^^^^^^^^ definition local9
//                                                      ^^^^^^^^^ definition local11
//                                                                  ^^^^^^^^ reference androidx/annotation/Nullable#
//                                                                           ^^^^^^ reference java/lang/Object#
//                                                                                  ^^^^^^^ definition local13
    if (payload == null) {
//      ^^^^^^^ reference local15
      onItemRangeChanged(positionStart, itemCount);
//    ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyDiffLogger#onItemRangeChanged().
//                       ^^^^^^^^^^^^^ reference local16
//                                      ^^^^^^^^^ reference local17
    } else {
      Log.d(tag,
//    ^^^ reference _root_/
//        ^ reference d#
//          ^^^ reference com/airbnb/epoxy/EpoxyDiffLogger#tag.
          "Item range changed with payloads. Start: " + positionStart + " Count: " + itemCount);
//                                                      ^^^^^^^^^^^^^ reference local18
//                                                                                   ^^^^^^^^^ reference local19
    }
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onItemRangeInserted(int positionStart, int itemCount) {
//            ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyDiffLogger#onItemRangeInserted().
//                                    ^^^^^^^^^^^^^ definition local20
//                                                       ^^^^^^^^^ definition local22
    Log.d(tag, "Item range inserted. Start: " + positionStart + " Count: " + itemCount);
//  ^^^ reference _root_/
//      ^ reference d#
//        ^^^ reference com/airbnb/epoxy/EpoxyDiffLogger#tag.
//                                              ^^^^^^^^^^^^^ reference local24
//                                                                           ^^^^^^^^^ reference local25
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onItemRangeRemoved(int positionStart, int itemCount) {
//            ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyDiffLogger#onItemRangeRemoved().
//                                   ^^^^^^^^^^^^^ definition local26
//                                                      ^^^^^^^^^ definition local28
    Log.d(tag, "Item range removed. Start: " + positionStart + " Count: " + itemCount);
//  ^^^ reference _root_/
//      ^ reference d#
//        ^^^ reference com/airbnb/epoxy/EpoxyDiffLogger#tag.
//                                             ^^^^^^^^^^^^^ reference local30
//                                                                          ^^^^^^^^^ reference local31
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
//            ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyDiffLogger#onItemRangeMoved().
//                                 ^^^^^^^^^^^^ definition local32
//                                                   ^^^^^^^^^^ definition local34
//                                                                   ^^^^^^^^^ definition local36
    Log.d(tag, "Item moved. From: " + fromPosition + " To: " + toPosition);
//  ^^^ reference _root_/
//      ^ reference d#
//        ^^^ reference com/airbnb/epoxy/EpoxyDiffLogger#tag.
//                                    ^^^^^^^^^^^^ reference local38
//                                                             ^^^^^^^^^^ reference local39
  }
}

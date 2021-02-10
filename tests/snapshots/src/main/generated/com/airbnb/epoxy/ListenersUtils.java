package com.airbnb.epoxy;

import android.view.View;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^ reference android/view/View#
import android.view.ViewParent;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^^^^^^^ reference android/view/ViewParent#

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#
import androidx.recyclerview.widget.RecyclerView;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView#
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView/
//                                               ^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView/ViewHolder#

public class ListenersUtils {
//     ^^^^^^ definition com/airbnb/epoxy/ListenersUtils#`<init>`().
//     ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ListenersUtils#

  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  static EpoxyViewHolder getEpoxyHolderForChildView(View v) {
//       ^^^^^^^^^^^^^^^ reference _root_/
//                       ^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ListenersUtils#getEpoxyHolderForChildView().
//                                                  ^^^^ reference _root_/
//                                                       ^ definition local0
    RecyclerView recyclerView = findParentRecyclerView(v);
//  ^^^^^^^^^^^^ reference _root_/
//               ^^^^^^^^^^^^ definition local2
//                              ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ListenersUtils#findParentRecyclerView().
//                                                     ^ reference local4
    if (recyclerView == null) {
//      ^^^^^^^^^^^^ reference local5
      return null;
    }

    ViewHolder viewHolder = recyclerView.findContainingViewHolder(v);
//  ^^^^^^^^^^ reference _root_/
//             ^^^^^^^^^^ definition local6
//                          ^^^^^^^^^^^^ reference local8
//                                       ^^^^^^^^^^^^^^^^^^^^^^^^ reference findContainingViewHolder#
//                                                                ^ reference local9
    if (viewHolder == null) {
//      ^^^^^^^^^^ reference local10
      return null;
    }

    if (!(viewHolder instanceof EpoxyViewHolder)) {
//        ^^^^^^^^^^ reference local11
//                              ^^^^^^^^^^^^^^^ reference _root_/
      return null;
    }

    return (EpoxyViewHolder) viewHolder;
//          ^^^^^^^^^^^^^^^ reference _root_/
//                           ^^^^^^^^^^ reference local12
  }

  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  private static RecyclerView findParentRecyclerView(@Nullable View v) {
//               ^^^^^^^^^^^^ reference _root_/
//                            ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ListenersUtils#findParentRecyclerView().
//                                                    ^^^^^^^^ reference androidx/annotation/Nullable#
//                                                             ^^^^ reference _root_/
//                                                                  ^ definition local13
    if (v == null) {
//      ^ reference local15
      return null;
    }

    ViewParent parent = v.getParent();
//  ^^^^^^^^^^ reference _root_/
//             ^^^^^^ definition local16
//                      ^ reference local18
//                        ^^^^^^^^^ reference getParent#
    if (parent instanceof RecyclerView) {
//      ^^^^^^ reference local19
//                        ^^^^^^^^^^^^ reference _root_/
      return (RecyclerView) parent;
//            ^^^^^^^^^^^^ reference _root_/
//                          ^^^^^^ reference local20
    }

    if (parent instanceof View) {
//      ^^^^^^ reference local21
//                        ^^^^ reference _root_/
      return findParentRecyclerView((View) parent);
//           ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ListenersUtils#findParentRecyclerView().
//                                   ^^^^ reference _root_/
//                                         ^^^^^^ reference local22
    }

    return null;
  }
}

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
//           ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ListenersUtils# public class ListenersUtils
//           ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ListenersUtils#`<init>`(). public ListenersUtils()

  @Nullable
// ^^^^^^^^ reference androidx/annotation/Nullable#
  static EpoxyViewHolder getEpoxyHolderForChildView(View v) {
//       ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#
//                       ^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ListenersUtils#getEpoxyHolderForChildView(). @Nullable static EpoxyViewHolder getEpoxyHolderForChildView(unresolved_type v)
//                                                  ^^^^ reference _root_/
//                                                       ^ definition local0 unresolved_type v
    RecyclerView recyclerView = findParentRecyclerView(v);
//  ^^^^^^^^^^^^ reference _root_/
//               ^^^^^^^^^^^^ definition local1 unresolved_type recyclerView
//                              ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ListenersUtils#findParentRecyclerView().
//                                                     ^ reference local0
    if (recyclerView == null) {
//      ^^^^^^^^^^^^ reference local1
      return null;
    }

    ViewHolder viewHolder = recyclerView.findContainingViewHolder(v);
//  ^^^^^^^^^^ reference _root_/
//             ^^^^^^^^^^ definition local2 unresolved_type viewHolder
//                          ^^^^^^^^^^^^ reference local1
//                                       ^^^^^^^^^^^^^^^^^^^^^^^^ reference findContainingViewHolder#
//                                                                ^ reference local0
    if (viewHolder == null) {
//      ^^^^^^^^^^ reference local2
      return null;
    }

    if (!(viewHolder instanceof EpoxyViewHolder)) {
//        ^^^^^^^^^^ reference local2
//                              ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#
      return null;
    }

    return (EpoxyViewHolder) viewHolder;
//          ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#
//                           ^^^^^^^^^^ reference local2
  }

  @Nullable
// ^^^^^^^^ reference androidx/annotation/Nullable#
  private static RecyclerView findParentRecyclerView(@Nullable View v) {
//               ^^^^^^^^^^^^ reference _root_/
//                            ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ListenersUtils#findParentRecyclerView(). @Nullable private static unresolved_type findParentRecyclerView(unresolved_type v)
//                                                    ^^^^^^^^ reference androidx/annotation/Nullable#
//                                                             ^^^^ reference _root_/
//                                                                  ^ definition local3 @Nullable unresolved_type v
    if (v == null) {
//      ^ reference local3
      return null;
    }

    ViewParent parent = v.getParent();
//  ^^^^^^^^^^ reference _root_/
//             ^^^^^^ definition local4 unresolved_type parent
//                      ^ reference local3
//                        ^^^^^^^^^ reference getParent#
    if (parent instanceof RecyclerView) {
//      ^^^^^^ reference local4
//                        ^^^^^^^^^^^^ reference _root_/
      return (RecyclerView) parent;
//            ^^^^^^^^^^^^ reference _root_/
//                          ^^^^^^ reference local4
    }

    if (parent instanceof View) {
//      ^^^^^^ reference local4
//                        ^^^^ reference _root_/
      return findParentRecyclerView((View) parent);
//           ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ListenersUtils#findParentRecyclerView().
//                                   ^^^^ reference _root_/
//                                         ^^^^^^ reference local4
    }

    return null;
  }
}

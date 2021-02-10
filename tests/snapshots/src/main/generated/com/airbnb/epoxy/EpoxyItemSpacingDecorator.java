package com.airbnb.epoxy;

import android.graphics.Rect;
//     ^^^^^^^ reference android/
//             ^^^^^^^^ reference android/graphics/
//                      ^^^^ reference android/graphics/Rect#
import android.view.View;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^ reference android/view/View#

import androidx.annotation.Px;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^ reference androidx/annotation/Px#
import androidx.core.view.ViewCompat;
//     ^^^^^^^^ reference androidx/
//              ^^^^ reference androidx/core/
//                   ^^^^ reference androidx/core/view/
//                        ^^^^^^^^^^ reference androidx/core/view/ViewCompat#
import androidx.recyclerview.widget.GridLayoutManager;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^^^^^^ reference androidx/recyclerview/widget/GridLayoutManager#
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^^^^^^ reference androidx/recyclerview/widget/GridLayoutManager/
//                                                    ^^^^^^^^^^^^^^ reference androidx/recyclerview/widget/GridLayoutManager/SpanSizeLookup#
import androidx.recyclerview.widget.LinearLayoutManager;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^^^^^^^^ reference androidx/recyclerview/widget/LinearLayoutManager#
import androidx.recyclerview.widget.RecyclerView;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView#
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView/
//                                               ^^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView/LayoutManager#
import androidx.recyclerview.widget.RecyclerView.State;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView/
//                                               ^^^^^ reference androidx/recyclerview/widget/RecyclerView/State#

/**
 * Modifies item spacing in a recycler view so that items are equally spaced no matter where they
 * are on the grid. Only designed to work with standard linear or grid layout managers.
 */
public class EpoxyItemSpacingDecorator extends RecyclerView.ItemDecoration {
//     ^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#
//                                             ^^^^^^^^^^^^ reference RecyclerView/
//                                                          ^^^^^^^^^^^^^^ reference RecyclerView/ItemDecoration#
  private int pxBetweenItems;
//            ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#pxBetweenItems.
  private boolean verticallyScrolling;
//                ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#verticallyScrolling.
  private boolean horizontallyScrolling;
//                ^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
  private boolean firstItem;
//                ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#firstItem.
  private boolean lastItem;
//                ^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#lastItem.
  private boolean grid;
//                ^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#grid.

  private boolean isFirstItemInRow;
//                ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#isFirstItemInRow.
  private boolean fillsLastSpan;
//                ^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#fillsLastSpan.
  private boolean isInFirstRow;
//                ^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInFirstRow.
  private boolean isInLastRow;
//                ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInLastRow.

  public EpoxyItemSpacingDecorator() {
//       ^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#`<init>`().
    this(0);
//  ^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#`<init>`(+1).
  }

  public EpoxyItemSpacingDecorator(@Px int pxBetweenItems) {
//       ^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#`<init>`(+1).
//                                  ^^ reference androidx/annotation/Px#
//                                         ^^^^^^^^^^^^^^ definition local0
    setPxBetweenItems(pxBetweenItems);
//  ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#setPxBetweenItems().
//                    ^^^^^^^^^^^^^^ reference local2
  }

  public void setPxBetweenItems(@Px int pxBetweenItems) {
//            ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#setPxBetweenItems().
//                               ^^ reference androidx/annotation/Px#
//                                      ^^^^^^^^^^^^^^ definition local3
    this.pxBetweenItems = pxBetweenItems;
//  ^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#this.
//       ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#pxBetweenItems.
//                        ^^^^^^^^^^^^^^ reference local5
  }

  @Px
   ^^ reference androidx/annotation/Px#
  public int getPxBetweenItems() {
//           ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#getPxBetweenItems().
    return pxBetweenItems;
//         ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#pxBetweenItems.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
//            ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#getItemOffsets().
//                           ^^^^ reference _root_/
//                                ^^^^^^^ definition local6
//                                         ^^^^ reference _root_/
//                                              ^^^^ definition local8
//                                                    ^^^^^^^^^^^^ reference _root_/
//                                                                 ^^^^^^ definition local10
//                                                                         ^^^^^ reference _root_/
//                                                                               ^^^^^ definition local12
    // Zero everything out for the common case
    outRect.setEmpty();
//  ^^^^^^^ reference local14
//          ^^^^^^^^ reference setEmpty#

    int position = parent.getChildAdapterPosition(view);
//      ^^^^^^^^ definition local15
//                 ^^^^^^ reference local17
//                        ^^^^^^^^^^^^^^^^^^^^^^^ reference getChildAdapterPosition#
//                                                ^^^^ reference local18
    if (position == RecyclerView.NO_POSITION) {
//      ^^^^^^^^ reference local19
//                  ^^^^^^^^^^^^ reference _root_/
//                               ^^^^^^^^^^^ reference NO_POSITION#
      // View is not shown
      return;
    }

    RecyclerView.LayoutManager layout = parent.getLayoutManager();
//  ^^^^^^^^^^^^ reference RecyclerView/
//               ^^^^^^^^^^^^^ reference RecyclerView/LayoutManager#
//                             ^^^^^^ definition local20
//                                      ^^^^^^ reference local22
//                                             ^^^^^^^^^^^^^^^^ reference getLayoutManager#
    calculatePositionDetails(parent, position, layout);
//  ^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#calculatePositionDetails().
//                           ^^^^^^ reference local23
//                                   ^^^^^^^^ reference local24
//                                             ^^^^^^ reference local25

    boolean left = useLeftPadding();
//          ^^^^ definition local26
//                 ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#useLeftPadding().
    boolean right = useRightPadding();
//          ^^^^^ definition local28
//                  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#useRightPadding().
    boolean top = useTopPadding();
//          ^^^ definition local30
//                ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#useTopPadding().
    boolean bottom = useBottomPadding();
//          ^^^^^^ definition local32
//                   ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#useBottomPadding().

    if (shouldReverseLayout(layout, horizontallyScrolling)) {
//      ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#shouldReverseLayout().
//                          ^^^^^^ reference local34
//                                  ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
      if (horizontallyScrolling) {
//        ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
        boolean temp = left;
//              ^^^^ definition local35
//                     ^^^^ reference local37
        left = right;
//      ^^^^ reference local38
//             ^^^^^ reference local39
        right = temp;
//      ^^^^^ reference local40
//              ^^^^ reference local41
      } else {
        boolean temp = top;
//              ^^^^ definition local42
//                     ^^^ reference local44
        top = bottom;
//      ^^^ reference local45
//            ^^^^^^ reference local46
        bottom = temp;
//      ^^^^^^ reference local47
//               ^^^^ reference local48
      }
    }

    // Divided by two because it is applied to the left side of one item and the right of another
    // to add up to the total desired space
    int padding = pxBetweenItems / 2;
//      ^^^^^^^ definition local49
//                ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#pxBetweenItems.
    outRect.right = right ? padding : 0;
//  ^^^^^^^ reference local51
//          ^^^^^ reference right#
//                  ^^^^^ reference local52
//                          ^^^^^^^ reference local53
    outRect.left = left ? padding : 0;
//  ^^^^^^^ reference local54
//          ^^^^ reference left#
//                 ^^^^ reference local55
//                        ^^^^^^^ reference local56
    outRect.top = top ? padding : 0;
//  ^^^^^^^ reference local57
//          ^^^ reference top#
//                ^^^ reference local58
//                      ^^^^^^^ reference local59
    outRect.bottom = bottom ? padding : 0;
//  ^^^^^^^ reference local60
//          ^^^^^^ reference bottom#
//                   ^^^^^^ reference local61
//                            ^^^^^^^ reference local62
  }

  private void calculatePositionDetails(RecyclerView parent, int position, LayoutManager layout) {
//             ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#calculatePositionDetails().
//                                      ^^^^^^^^^^^^ reference _root_/
//                                                   ^^^^^^ definition local63
//                                                               ^^^^^^^^ definition local65
//                                                                         ^^^^^^^^^^^^^ reference _root_/
//                                                                                       ^^^^^^ definition local67
    int itemCount = parent.getAdapter().getItemCount();
//      ^^^^^^^^^ definition local69
//                  ^^^^^^ reference local71
//                         ^^^^^^^^^^ reference getAdapter#
//                                      ^^^^^^^^^^^^ reference getAdapter#getItemCount#
    firstItem = position == 0;
//  ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#firstItem.
//              ^^^^^^^^ reference local72
    lastItem = position == itemCount - 1;
//  ^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#lastItem.
//             ^^^^^^^^ reference local73
//                         ^^^^^^^^^ reference local74
    horizontallyScrolling = layout.canScrollHorizontally();
//  ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
//                          ^^^^^^ reference local75
//                                 ^^^^^^^^^^^^^^^^^^^^^ reference canScrollHorizontally#
    verticallyScrolling = layout.canScrollVertically();
//  ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#verticallyScrolling.
//                        ^^^^^^ reference local76
//                               ^^^^^^^^^^^^^^^^^^^ reference canScrollVertically#
    grid = layout instanceof GridLayoutManager;
//  ^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#grid.
//         ^^^^^^ reference local77
//                           ^^^^^^^^^^^^^^^^^ reference _root_/

    if (grid) {
//      ^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#grid.
      GridLayoutManager grid = (GridLayoutManager) layout;
//    ^^^^^^^^^^^^^^^^^ reference _root_/
//                      ^^^^ definition local78
//                              ^^^^^^^^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^ reference local80
      final SpanSizeLookup spanSizeLookup = grid.getSpanSizeLookup();
//          ^^^^^^^^^^^^^^ reference _root_/
//                         ^^^^^^^^^^^^^^ definition local81
//                                          ^^^^ reference local83
//                                               ^^^^^^^^^^^^^^^^^ reference getSpanSizeLookup#
      int spanSize = spanSizeLookup.getSpanSize(position);
//        ^^^^^^^^ definition local84
//                   ^^^^^^^^^^^^^^ reference local86
//                                  ^^^^^^^^^^^ reference getSpanSize#
//                                              ^^^^^^^^ reference local87
      int spanCount = grid.getSpanCount();
//        ^^^^^^^^^ definition local88
//                    ^^^^ reference local90
//                         ^^^^^^^^^^^^ reference getSpanCount#
      int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
//        ^^^^^^^^^ definition local91
//                    ^^^^^^^^^^^^^^ reference local93
//                                   ^^^^^^^^^^^^ reference getSpanIndex#
//                                                ^^^^^^^^ reference local94
//                                                          ^^^^^^^^^ reference local95
      isFirstItemInRow = spanIndex == 0;
//    ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isFirstItemInRow.
//                       ^^^^^^^^^ reference local96
      fillsLastSpan = spanIndex + spanSize == spanCount;
//    ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#fillsLastSpan.
//                    ^^^^^^^^^ reference local97
//                                ^^^^^^^^ reference local98
//                                            ^^^^^^^^^ reference local99
      isInFirstRow = isInFirstRow(position, spanSizeLookup, spanCount);
//    ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInFirstRow.
//                   ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInFirstRow(+1).
//                                ^^^^^^^^ reference local100
//                                          ^^^^^^^^^^^^^^ reference local101
//                                                          ^^^^^^^^^ reference local102
      isInLastRow =
//    ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInLastRow.
          !isInFirstRow && isInLastRow(position, itemCount, spanSizeLookup, spanCount);
//         ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInFirstRow.
//                         ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInLastRow(+1).
//                                     ^^^^^^^^ reference local103
//                                               ^^^^^^^^^ reference local104
//                                                          ^^^^^^^^^^^^^^ reference local105
//                                                                          ^^^^^^^^^ reference local106
    }
  }

  private static boolean shouldReverseLayout(LayoutManager layout, boolean horizontallyScrolling) {
//                       ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#shouldReverseLayout().
//                                           ^^^^^^^^^^^^^ reference _root_/
//                                                         ^^^^^^ definition local107
//                                                                         ^^^^^^^^^^^^^^^^^^^^^ definition local109
    boolean reverseLayout =
//          ^^^^^^^^^^^^^ definition local111
        layout instanceof LinearLayoutManager && ((LinearLayoutManager) layout).getReverseLayout();
//      ^^^^^^ reference local113
//                        ^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                                                      ^^^^^^ reference local114
//                                                                              ^^^^^^^^^^^^^^^^ reference getReverseLayout#
    boolean rtl = layout.getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL;
//          ^^^ definition local115
//                ^^^^^^ reference local117
//                       ^^^^^^^^^^^^^^^^^^ reference getLayoutDirection#
//                                               ^^^^^^^^^^ reference _root_/
//                                                          ^^^^^^^^^^^^^^^^^^^^ reference LAYOUT_DIRECTION_RTL#
    if (horizontallyScrolling && rtl) {
//      ^^^^^^^^^^^^^^^^^^^^^ reference local118
//                               ^^^ reference local119
      // This is how linearlayout checks if it should reverse layout in #resolveShouldLayoutReverse
      reverseLayout = !reverseLayout;
//    ^^^^^^^^^^^^^ reference local120
//                     ^^^^^^^^^^^^^ reference local121
    }

    return reverseLayout;
//         ^^^^^^^^^^^^^ reference local122
  }

  private boolean useBottomPadding() {
//                ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#useBottomPadding().
    if (grid) {
//      ^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#grid.
      return (horizontallyScrolling && !fillsLastSpan)
//            ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
//                                      ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#fillsLastSpan.
          || (verticallyScrolling && !isInLastRow);
//            ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#verticallyScrolling.
//                                    ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInLastRow.
    }

    return verticallyScrolling && !lastItem;
//         ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#verticallyScrolling.
//                                 ^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#lastItem.
  }

  private boolean useTopPadding() {
//                ^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#useTopPadding().
    if (grid) {
//      ^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#grid.
      return (horizontallyScrolling && !isFirstItemInRow)
//            ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
//                                      ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isFirstItemInRow.
          || (verticallyScrolling && !isInFirstRow);
//            ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#verticallyScrolling.
//                                    ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInFirstRow.
    }

    return verticallyScrolling && !firstItem;
//         ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#verticallyScrolling.
//                                 ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#firstItem.
  }

  private boolean useRightPadding() {
//                ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#useRightPadding().
    if (grid) {
//      ^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#grid.
      return (horizontallyScrolling && !isInLastRow)
//            ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
//                                      ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInLastRow.
          || (verticallyScrolling && !fillsLastSpan);
//            ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#verticallyScrolling.
//                                    ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#fillsLastSpan.
    }

    return horizontallyScrolling && !lastItem;
//         ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
//                                   ^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#lastItem.
  }

  private boolean useLeftPadding() {
//                ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#useLeftPadding().
    if (grid) {
//      ^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#grid.
      return (horizontallyScrolling && !isInFirstRow)
//            ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
//                                      ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInFirstRow.
          || (verticallyScrolling && !isFirstItemInRow);
//            ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#verticallyScrolling.
//                                    ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#isFirstItemInRow.
    }

    return horizontallyScrolling && !firstItem;
//         ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#horizontallyScrolling.
//                                   ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyItemSpacingDecorator#firstItem.
  }

  private static boolean isInFirstRow(int position, SpanSizeLookup spanSizeLookup, int spanCount) {
//                       ^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInFirstRow(+1).
//                                        ^^^^^^^^ definition local123
//                                                  ^^^^^^^^^^^^^^ reference _root_/
//                                                                 ^^^^^^^^^^^^^^ definition local125
//                                                                                     ^^^^^^^^^ definition local127
    int totalSpan = 0;
//      ^^^^^^^^^ definition local129
    for (int i = 0; i <= position; i++) {
//           ^ definition local131
//                  ^ reference local133
//                       ^^^^^^^^ reference local134
//                                 ^ reference local135
      totalSpan += spanSizeLookup.getSpanSize(i);
//    ^^^^^^^^^ reference local136
//                 ^^^^^^^^^^^^^^ reference local137
//                                ^^^^^^^^^^^ reference getSpanSize#
//                                            ^ reference local138
      if (totalSpan > spanCount) {
//        ^^^^^^^^^ reference local139
//                    ^^^^^^^^^ reference local140
        return false;
      }
    }

    return true;
  }

  private static boolean isInLastRow(int position, int itemCount, SpanSizeLookup spanSizeLookup,
//                       ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyItemSpacingDecorator#isInLastRow(+1).
//                                       ^^^^^^^^ definition local141
//                                                     ^^^^^^^^^ definition local143
//                                                                ^^^^^^^^^^^^^^ reference _root_/
//                                                                               ^^^^^^^^^^^^^^ definition local145
      int spanCount) {
//        ^^^^^^^^^ definition local147
    int totalSpan = 0;
//      ^^^^^^^^^ definition local149
    for (int i = itemCount - 1; i >= position; i--) {
//           ^ definition local151
//               ^^^^^^^^^ reference local153
//                              ^ reference local154
//                                   ^^^^^^^^ reference local155
//                                             ^ reference local156
      totalSpan += spanSizeLookup.getSpanSize(i);
//    ^^^^^^^^^ reference local157
//                 ^^^^^^^^^^^^^^ reference local158
//                                ^^^^^^^^^^^ reference getSpanSize#
//                                            ^ reference local159
      if (totalSpan > spanCount) {
//        ^^^^^^^^^ reference local160
//                    ^^^^^^^^^ reference local161
        return false;
      }
    }

    return true;
  }
}

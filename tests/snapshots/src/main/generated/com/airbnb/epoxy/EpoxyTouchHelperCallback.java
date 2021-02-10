package com.airbnb.epoxy;

import android.graphics.Canvas;
//     ^^^^^^^ reference android/
//             ^^^^^^^^ reference android/graphics/
//                      ^^^^^^ reference android/graphics/Canvas#

import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#

import androidx.recyclerview.widget.ItemTouchHelper;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^^^^ reference androidx/recyclerview/widget/ItemTouchHelper#
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

/**
 * A wrapper around {@link androidx.recyclerview.widget.ItemTouchHelper.Callback} to cast all
 * view holders to {@link com.airbnb.epoxy.EpoxyViewHolder} for simpler use with Epoxy.
 */
public abstract class EpoxyTouchHelperCallback extends ItemTouchHelper.Callback {
//              ^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#
//                                                     ^^^^^^^^^^^^^^^ reference ItemTouchHelper/
//                                                                     ^^^^^^^^ reference ItemTouchHelper/Callback#

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
//                 ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#getMovementFlags().
//                                  ^^^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^^^ definition local0
//                                                             ^^^^^^^^^^ reference _root_/
//                                                                        ^^^^^^^^^^ definition local2
    return getMovementFlags(recyclerView, (EpoxyViewHolder) viewHolder);
//         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#getMovementFlags().
//                          ^^^^^^^^^^^^ reference local4
//                                         ^^^^^^^^^^^^^^^ reference _root_/
//                                                          ^^^^^^^^^^ reference local5
  }

  /**
   * @see #getMovementFlags(RecyclerView, ViewHolder)
   */
  protected abstract int getMovementFlags(RecyclerView recyclerView, EpoxyViewHolder viewHolder);
//                       ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#getMovementFlags(+1).
//                                        ^^^^^^^^^^^^ reference _root_/
//                                                     ^^^^^^^^^^^^ definition local6
//                                                                   ^^^^^^^^^^^^^^^ reference _root_/
//                                                                                   ^^^^^^^^^^ definition local8

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
//                     ^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onMove().
//                            ^^^^^^^^^^^^ reference _root_/
//                                         ^^^^^^^^^^^^ definition local10
//                                                       ^^^^^^^^^^ reference _root_/
//                                                                  ^^^^^^^^^^ definition local12
//                                                                              ^^^^^^^^^^ reference _root_/
//                                                                                         ^^^^^^ definition local14
    return onMove(recyclerView, (EpoxyViewHolder) viewHolder, (EpoxyViewHolder) target);
//         ^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#onMove().
//                ^^^^^^^^^^^^ reference local16
//                               ^^^^^^^^^^^^^^^ reference _root_/
//                                                ^^^^^^^^^^ reference local17
//                                                             ^^^^^^^^^^^^^^^ reference _root_/
//                                                                              ^^^^^^ reference local18
  }

  /**
   * @see #onMove(RecyclerView, ViewHolder, ViewHolder)
   */
  protected abstract boolean onMove(RecyclerView recyclerView, EpoxyViewHolder viewHolder,
//                           ^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onMove(+1).
//                                  ^^^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^^^ definition local19
//                                                             ^^^^^^^^^^^^^^^ reference _root_/
//                                                                             ^^^^^^^^^^ definition local21
      EpoxyViewHolder target);
//    ^^^^^^^^^^^^^^^ reference _root_/
//                    ^^^^^^ definition local23

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void onSwiped(ViewHolder viewHolder, int direction) {
//                  ^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onSwiped().
//                           ^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^^^^ definition local25
//                                                      ^^^^^^^^^ definition local27
    onSwiped((EpoxyViewHolder) viewHolder, direction);
//  ^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#onSwiped().
//            ^^^^^^^^^^^^^^^ reference _root_/
//                             ^^^^^^^^^^ reference local29
//                                         ^^^^^^^^^ reference local30
  }

  /**
   * @see #onSwiped(ViewHolder, int)
   */
  protected abstract void onSwiped(EpoxyViewHolder viewHolder, int direction);
//                        ^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onSwiped(+1).
//                                 ^^^^^^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^^^^^ definition local31
//                                                                 ^^^^^^^^^ definition local33

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final boolean canDropOver(RecyclerView recyclerView, ViewHolder current,
//                     ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#canDropOver().
//                                 ^^^^^^^^^^^^ reference _root_/
//                                              ^^^^^^^^^^^^ definition local35
//                                                            ^^^^^^^^^^ reference _root_/
//                                                                       ^^^^^^^ definition local37
      ViewHolder target) {
//    ^^^^^^^^^^ reference _root_/
//               ^^^^^^ definition local39
    return canDropOver(recyclerView, (EpoxyViewHolder) current, (EpoxyViewHolder) target);
//         ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#canDropOver().
//                     ^^^^^^^^^^^^ reference local41
//                                    ^^^^^^^^^^^^^^^ reference _root_/
//                                                     ^^^^^^^ reference local42
//                                                               ^^^^^^^^^^^^^^^ reference _root_/
//                                                                                ^^^^^^ reference local43
  }

  /**
   * @see #canDropOver(RecyclerView, ViewHolder, ViewHolder)
   */
  protected boolean canDropOver(RecyclerView recyclerView, EpoxyViewHolder current,
//                  ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#canDropOver(+1).
//                              ^^^^^^^^^^^^ reference _root_/
//                                           ^^^^^^^^^^^^ definition local44
//                                                         ^^^^^^^^^^^^^^^ reference _root_/
//                                                                         ^^^^^^^ definition local46
      EpoxyViewHolder target) {
//    ^^^^^^^^^^^^^^^ reference _root_/
//                    ^^^^^^ definition local48
    return super.canDropOver(recyclerView, current, target);
//         ^^^^^ reference _root_/
//               ^^^^^^^^^^^ reference canDropOver#
//                           ^^^^^^^^^^^^ reference local50
//                                         ^^^^^^^ reference local51
//                                                  ^^^^^^ reference local52
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final float getSwipeThreshold(ViewHolder viewHolder) {
//                   ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#getSwipeThreshold().
//                                     ^^^^^^^^^^ reference _root_/
//                                                ^^^^^^^^^^ definition local53
    return getSwipeThreshold((EpoxyViewHolder) viewHolder);
//         ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#getSwipeThreshold().
//                            ^^^^^^^^^^^^^^^ reference _root_/
//                                             ^^^^^^^^^^ reference local55
  }

  /**
   * @see #getSwipeThreshold(ViewHolder)
   */
  protected float getSwipeThreshold(EpoxyViewHolder viewHolder) {
//                ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#getSwipeThreshold(+1).
//                                  ^^^^^^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^^^^^ definition local56
    return super.getSwipeThreshold(viewHolder);
//         ^^^^^ reference _root_/
//               ^^^^^^^^^^^^^^^^^ reference getSwipeThreshold#
//                                 ^^^^^^^^^^ reference local58
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final float getMoveThreshold(ViewHolder viewHolder) {
//                   ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#getMoveThreshold().
//                                    ^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^ definition local59
    return getMoveThreshold((EpoxyViewHolder) viewHolder);
//         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#getMoveThreshold().
//                           ^^^^^^^^^^^^^^^ reference _root_/
//                                            ^^^^^^^^^^ reference local61
  }

  /**
   * @see #getMoveThreshold(ViewHolder)
   */
  protected float getMoveThreshold(EpoxyViewHolder viewHolder) {
//                ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#getMoveThreshold(+1).
//                                 ^^^^^^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^^^^^ definition local62
    return super.getMoveThreshold(viewHolder);
//         ^^^^^ reference _root_/
//               ^^^^^^^^^^^^^^^^ reference getMoveThreshold#
//                                ^^^^^^^^^^ reference local64
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final ViewHolder chooseDropTarget(ViewHolder selected, List dropTargets, int curX,
//             ^^^^^^^^^^ reference _root_/
//                        ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#chooseDropTarget().
//                                         ^^^^^^^^^^ reference _root_/
//                                                    ^^^^^^^^ definition local65
//                                                              ^^^^ reference java/util/List#
//                                                                   ^^^^^^^^^^^ definition local67
//                                                                                    ^^^^ definition local69
      int curY) {
//        ^^^^ definition local71
    //noinspection unchecked
    return chooseDropTarget((EpoxyViewHolder) selected, (List<EpoxyViewHolder>) dropTargets, curX,
//         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#chooseDropTarget(+1).
//                           ^^^^^^^^^^^^^^^ reference _root_/
//                                            ^^^^^^^^ reference local73
//                                                       ^^^^ reference java/util/List#
//                                                            ^^^^^^^^^^^^^^^ reference _root_/
//                                                                              ^^^^^^^^^^^ reference local74
//                                                                                           ^^^^ reference local75
        curY);
//      ^^^^ reference local76
  }

  /**
   * @see #chooseDropTarget(ViewHolder, List, int, int)
   */
  protected EpoxyViewHolder chooseDropTarget(EpoxyViewHolder selected,
//          ^^^^^^^^^^^^^^^ reference _root_/
//                          ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#chooseDropTarget(+1).
//                                           ^^^^^^^^^^^^^^^ reference _root_/
//                                                           ^^^^^^^^ definition local77
      List<EpoxyViewHolder> dropTargets, int curX, int curY) {
//    ^^^^ reference java/util/List#
//         ^^^^^^^^^^^^^^^ reference _root_/
//                          ^^^^^^^^^^^ definition local79
//                                           ^^^^ definition local81
//                                                     ^^^^ definition local83

    //noinspection unchecked
    return (EpoxyViewHolder) super.chooseDropTarget(selected, (List) dropTargets, curX, curY);
//          ^^^^^^^^^^^^^^^ reference _root_/
//                           ^^^^^ reference _root_/
//                                 ^^^^^^^^^^^^^^^^ reference chooseDropTarget#
//                                                  ^^^^^^^^ reference local85
//                                                             ^^^^ reference java/util/List#
//                                                                   ^^^^^^^^^^^ reference local86
//                                                                                ^^^^ reference local87
//                                                                                      ^^^^ reference local88
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void onSelectedChanged(ViewHolder viewHolder, int actionState) {
//                  ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onSelectedChanged().
//                                    ^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^ definition local89
//                                                               ^^^^^^^^^^^ definition local91
    onSelectedChanged((EpoxyViewHolder) viewHolder, actionState);
//  ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#onSelectedChanged().
//                     ^^^^^^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^^^^ reference local93
//                                                  ^^^^^^^^^^^ reference local94
  }

  /**
   * @see #onSelectedChanged(ViewHolder, int)
   */
  protected void onSelectedChanged(EpoxyViewHolder viewHolder, int actionState) {
//               ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onSelectedChanged(+1).
//                                 ^^^^^^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^^^^^ definition local95
//                                                                 ^^^^^^^^^^^ definition local97
    super.onSelectedChanged(viewHolder, actionState);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^ reference onSelectedChanged#
//                          ^^^^^^^^^^ reference local99
//                                      ^^^^^^^^^^^ reference local100
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void onMoved(RecyclerView recyclerView, ViewHolder viewHolder, int fromPos,
//                  ^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onMoved().
//                          ^^^^^^^^^^^^ reference _root_/
//                                       ^^^^^^^^^^^^ definition local101
//                                                     ^^^^^^^^^^ reference _root_/
//                                                                ^^^^^^^^^^ definition local103
//                                                                                ^^^^^^^ definition local105
      ViewHolder target, int toPos, int x, int y) {
//    ^^^^^^^^^^ reference _root_/
//               ^^^^^^ definition local107
//                           ^^^^^ definition local109
//                                      ^ definition local111
//                                             ^ definition local113

    onMoved(recyclerView, (EpoxyViewHolder) viewHolder, fromPos, (EpoxyViewHolder) target, toPos, x,
//  ^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#onMoved().
//          ^^^^^^^^^^^^ reference local115
//                         ^^^^^^^^^^^^^^^ reference _root_/
//                                          ^^^^^^^^^^ reference local116
//                                                      ^^^^^^^ reference local117
//                                                                ^^^^^^^^^^^^^^^ reference _root_/
//                                                                                 ^^^^^^ reference local118
//                                                                                         ^^^^^ reference local119
//                                                                                                ^ reference local120
        y);
//      ^ reference local121
  }

  /**
   * @see #onMoved(RecyclerView, ViewHolder, int, ViewHolder, int, int, int)
   */
  protected void onMoved(RecyclerView recyclerView, EpoxyViewHolder viewHolder, int fromPos,
//               ^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onMoved(+1).
//                       ^^^^^^^^^^^^ reference _root_/
//                                    ^^^^^^^^^^^^ definition local122
//                                                  ^^^^^^^^^^^^^^^ reference _root_/
//                                                                  ^^^^^^^^^^ definition local124
//                                                                                  ^^^^^^^ definition local126
      EpoxyViewHolder target, int toPos, int x, int y) {
//    ^^^^^^^^^^^^^^^ reference _root_/
//                    ^^^^^^ definition local128
//                                ^^^^^ definition local130
//                                           ^ definition local132
//                                                  ^ definition local134
    super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
//  ^^^^^ reference _root_/
//        ^^^^^^^ reference onMoved#
//                ^^^^^^^^^^^^ reference local136
//                              ^^^^^^^^^^ reference local137
//                                          ^^^^^^^ reference local138
//                                                   ^^^^^^ reference local139
//                                                           ^^^^^ reference local140
//                                                                  ^ reference local141
//                                                                     ^ reference local142
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
//                  ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#clearView().
//                            ^^^^^^^^^^^^ reference _root_/
//                                         ^^^^^^^^^^^^ definition local143
//                                                       ^^^^^^^^^^ reference _root_/
//                                                                  ^^^^^^^^^^ definition local145
    clearView(recyclerView, (EpoxyViewHolder) viewHolder);
//  ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#clearView().
//            ^^^^^^^^^^^^ reference local147
//                           ^^^^^^^^^^^^^^^ reference _root_/
//                                            ^^^^^^^^^^ reference local148
  }

  /**
   * @see #clearView(RecyclerView, ViewHolder)
   */
  protected void clearView(RecyclerView recyclerView, EpoxyViewHolder viewHolder) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#clearView(+1).
//                         ^^^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^^^^^^ definition local149
//                                                    ^^^^^^^^^^^^^^^ reference _root_/
//                                                                    ^^^^^^^^^^ definition local151
    super.clearView(recyclerView, viewHolder);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference clearView#
//                  ^^^^^^^^^^^^ reference local153
//                                ^^^^^^^^^^ reference local154
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder,
//                  ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onChildDraw().
//                              ^^^^^^ reference _root_/
//                                     ^ definition local155
//                                        ^^^^^^^^^^^^ reference _root_/
//                                                     ^^^^^^^^^^^^ definition local157
//                                                                   ^^^^^^^^^^ reference _root_/
//                                                                              ^^^^^^^^^^ definition local159
      float dX,
//          ^^ definition local161
      float dY, int actionState, boolean isCurrentlyActive) {
//          ^^ definition local163
//                  ^^^^^^^^^^^ definition local165
//                                       ^^^^^^^^^^^^^^^^^ definition local167

    onChildDraw(c, recyclerView, (EpoxyViewHolder) viewHolder, dX, dY, actionState,
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#onChildDraw().
//              ^ reference local169
//                 ^^^^^^^^^^^^ reference local170
//                                ^^^^^^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^^^^^ reference local171
//                                                             ^^ reference local172
//                                                                 ^^ reference local173
//                                                                     ^^^^^^^^^^^ reference local174
        isCurrentlyActive);
//      ^^^^^^^^^^^^^^^^^ reference local175
  }

  /**
   * @see #onChildDraw(Canvas, RecyclerView, ViewHolder, float, float, int, boolean)
   */
  protected void onChildDraw(Canvas c, RecyclerView recyclerView, EpoxyViewHolder viewHolder,
//               ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onChildDraw(+1).
//                           ^^^^^^ reference _root_/
//                                  ^ definition local176
//                                     ^^^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^^^^^^^ definition local178
//                                                                ^^^^^^^^^^^^^^^ reference _root_/
//                                                                                ^^^^^^^^^^ definition local180
      float dX, float dY, int actionState, boolean isCurrentlyActive) {
//          ^^ definition local182
//                    ^^ definition local184
//                            ^^^^^^^^^^^ definition local186
//                                                 ^^^^^^^^^^^^^^^^^ definition local188
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^ reference onChildDraw#
//                    ^ reference local190
//                       ^^^^^^^^^^^^ reference local191
//                                     ^^^^^^^^^^ reference local192
//                                                 ^^ reference local193
//                                                     ^^ reference local194
//                                                         ^^^^^^^^^^^ reference local195
//                                                                      ^^^^^^^^^^^^^^^^^ reference local196
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void onChildDrawOver(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder,
//                  ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onChildDrawOver().
//                                  ^^^^^^ reference _root_/
//                                         ^ definition local197
//                                            ^^^^^^^^^^^^ reference _root_/
//                                                         ^^^^^^^^^^^^ definition local199
//                                                                       ^^^^^^^^^^ reference _root_/
//                                                                                  ^^^^^^^^^^ definition local201
      float dX,
//          ^^ definition local203
      float dY, int actionState, boolean isCurrentlyActive) {
//          ^^ definition local205
//                  ^^^^^^^^^^^ definition local207
//                                       ^^^^^^^^^^^^^^^^^ definition local209

    onChildDrawOver(c, recyclerView, (EpoxyViewHolder) viewHolder, dX, dY, actionState,
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyTouchHelperCallback#onChildDrawOver().
//                  ^ reference local211
//                     ^^^^^^^^^^^^ reference local212
//                                    ^^^^^^^^^^^^^^^ reference _root_/
//                                                     ^^^^^^^^^^ reference local213
//                                                                 ^^ reference local214
//                                                                     ^^ reference local215
//                                                                         ^^^^^^^^^^^ reference local216
        isCurrentlyActive);
//      ^^^^^^^^^^^^^^^^^ reference local217
  }

  /**
   * @see #onChildDrawOver(Canvas, RecyclerView, ViewHolder, float, float, int, boolean)
   */
  protected void onChildDrawOver(Canvas c, RecyclerView recyclerView, EpoxyViewHolder viewHolder,
//               ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyTouchHelperCallback#onChildDrawOver(+1).
//                               ^^^^^^ reference _root_/
//                                      ^ definition local218
//                                         ^^^^^^^^^^^^ reference _root_/
//                                                      ^^^^^^^^^^^^ definition local220
//                                                                    ^^^^^^^^^^^^^^^ reference _root_/
//                                                                                    ^^^^^^^^^^ definition local222
      float dX, float dY, int actionState, boolean isCurrentlyActive) {
//          ^^ definition local224
//                    ^^ definition local226
//                            ^^^^^^^^^^^ definition local228
//                                                 ^^^^^^^^^^^^^^^^^ definition local230

    super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^ reference onChildDrawOver#
//                        ^ reference local232
//                           ^^^^^^^^^^^^ reference local233
//                                         ^^^^^^^^^^ reference local234
//                                                     ^^ reference local235
//                                                         ^^ reference local236
//                                                             ^^^^^^^^^^^ reference local237
//                                                                          ^^^^^^^^^^^^^^^^^ reference local238
  }
}

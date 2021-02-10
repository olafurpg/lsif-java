package com.airbnb.epoxy;

import android.graphics.Canvas;
//     ^^^^^^^ reference android/
//             ^^^^^^^^ reference android/graphics/
//                      ^^^^^^ reference android/graphics/Canvas#
import android.view.View;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^ reference android/view/View#

import com.airbnb.viewmodeladapter.R;
//     ^^^ reference com/
//         ^^^^^^ reference com/airbnb/
//                ^^^^^^^^^^^^^^^^ reference com/airbnb/viewmodeladapter/
//                                 ^ reference com/airbnb/viewmodeladapter/R#

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#
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

/**
 * A wrapper around {@link androidx.recyclerview.widget.ItemTouchHelper.Callback} to enable
 * easier touch support when working with Epoxy models.
 * <p>
 * For simplicity you can use {@link EpoxyTouchHelper} to set up touch handling via this class for
 * you instead of using this class directly. However, you may choose to use this class directly with
 * your own {@link ItemTouchHelper} if you need extra flexibility or customization.
 */
public abstract class EpoxyModelTouchCallback<T extends EpoxyModel>
//              ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#
//                                                      ^^^^^^^^^^ reference _root_/
    extends EpoxyTouchHelperCallback implements EpoxyDragCallback<T>, EpoxySwipeCallback<T> {
//          ^^^^^^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                              ^^^^^^^^^^^^^^^^^ reference _root_/
//                                                                ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                                                                    ^^^^^^^^^^^^^^^^^^ reference _root_/
//                                                                                       ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]

  private static final int TOUCH_DEBOUNCE_MILLIS = 300;
//                         ^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#TOUCH_DEBOUNCE_MILLIS.

  @Nullable private final EpoxyController controller;
   ^^^^^^^^ reference androidx/annotation/Nullable#
//                        ^^^^^^^^^^^^^^^ reference _root_/
//                                        ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#controller.
  private final Class<T> targetModelClass;
//              ^^^^^ reference java/lang/Class#
//                    ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                       ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#targetModelClass.
  private EpoxyViewHolder holderBeingDragged;
//        ^^^^^^^^^^^^^^^ reference _root_/
//                        ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingDragged.
  private EpoxyViewHolder holderBeingSwiped;
//        ^^^^^^^^^^^^^^^ reference _root_/
//                        ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingSwiped.

  public EpoxyModelTouchCallback(@Nullable EpoxyController controller, Class<T> targetModelClass) {
//       ^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#`<init>`().
//                                ^^^^^^^^ reference androidx/annotation/Nullable#
//                                         ^^^^^^^^^^^^^^^ reference _root_/
//                                                         ^^^^^^^^^^ definition local0
//                                                                     ^^^^^ reference java/lang/Class#
//                                                                           ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                                                                              ^^^^^^^^^^^^^^^^ definition local2
    this.controller = controller;
//  ^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#this.
//       ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#controller.
//                    ^^^^^^^^^^ reference local4
    this.targetModelClass = targetModelClass;
//  ^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#this.
//       ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#targetModelClass.
//                          ^^^^^^^^^^^^^^^^ reference local5
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected int getMovementFlags(RecyclerView recyclerView, EpoxyViewHolder viewHolder) {
//              ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#getMovementFlags().
//                               ^^^^^^^^^^^^ reference _root_/
//                                            ^^^^^^^^^^^^ definition local6
//                                                          ^^^^^^^^^^^^^^^ reference _root_/
//                                                                          ^^^^^^^^^^ definition local8
    EpoxyModel<?> model = viewHolder.getModel();
//  ^^^^^^^^^^ reference _root_/
//                ^^^^^ definition local10
//                        ^^^^^^^^^^ reference local12
//                                   ^^^^^^^^ reference getModel#

    // If multiple touch callbacks are registered on the recyclerview (to support combinations of
    // dragging and dropping) then we won't want to enable anything if another
    // callback has a view actively selected.
    boolean isOtherCallbackActive =
//          ^^^^^^^^^^^^^^^^^^^^^ definition local13
        holderBeingDragged == null
//      ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingDragged.
            && holderBeingSwiped == null
//             ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingSwiped.
            && recyclerViewHasSelection(recyclerView);
//             ^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#recyclerViewHasSelection().
//                                      ^^^^^^^^^^^^ reference local15

    if (!isOtherCallbackActive && isTouchableModel(model)) {
//       ^^^^^^^^^^^^^^^^^^^^^ reference local16
//                                ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#isTouchableModel().
//                                                 ^^^^^ reference local17
      //noinspection unchecked
      return getMovementFlagsForModel((T) model, viewHolder.getAdapterPosition());
//           ^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#getMovementFlagsForModel#
//                                     ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                                        ^^^^^ reference local18
//                                               ^^^^^^^^^^ reference local19
//                                                          ^^^^^^^^^^^^^^^^^^ reference getAdapterPosition#
    } else {
      return 0;
    }
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected boolean canDropOver(RecyclerView recyclerView, EpoxyViewHolder current,
//                  ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#canDropOver().
//                              ^^^^^^^^^^^^ reference _root_/
//                                           ^^^^^^^^^^^^ definition local20
//                                                         ^^^^^^^^^^^^^^^ reference _root_/
//                                                                         ^^^^^^^ definition local22
      EpoxyViewHolder target) {
//    ^^^^^^^^^^^^^^^ reference _root_/
//                    ^^^^^^ definition local24
    // By default we don't allow dropping on a model that isn't a drag target
    return isTouchableModel(target.getModel());
//         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#isTouchableModel().
//                          ^^^^^^ reference local26
//                                 ^^^^^^^^ reference getModel#
  }

  protected boolean isTouchableModel(EpoxyModel<?> model) {
//                  ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#isTouchableModel().
//                                   ^^^^^^^^^^ reference _root_/
//                                                 ^^^^^ definition local27
    return targetModelClass.isInstance(model);
//         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#targetModelClass.
//                          ^^^^^^^^^^ reference java/lang/Class#isInstance().
//                                     ^^^^^ reference local29
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected boolean onMove(RecyclerView recyclerView, EpoxyViewHolder viewHolder,
//                  ^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onMove().
//                         ^^^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^^^^^^ definition local30
//                                                    ^^^^^^^^^^^^^^^ reference _root_/
//                                                                    ^^^^^^^^^^ definition local32
      EpoxyViewHolder target) {
//    ^^^^^^^^^^^^^^^ reference _root_/
//                    ^^^^^^ definition local34

    if (controller == null) {
//      ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#controller.
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 1:84
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "A controller must be provided in the constructor if dragging is enabled");
    }

    int fromPosition = viewHolder.getAdapterPosition();
//      ^^^^^^^^^^^^ definition local36
//                     ^^^^^^^^^^ reference local38
//                                ^^^^^^^^^^^^^^^^^^ reference getAdapterPosition#
    int toPosition = target.getAdapterPosition();
//      ^^^^^^^^^^ definition local39
//                   ^^^^^^ reference local41
//                          ^^^^^^^^^^^^^^^^^^ reference getAdapterPosition#
    controller.moveModel(fromPosition, toPosition);
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#controller.
//             ^^^^^^^^^ reference moveModel#
//                       ^^^^^^^^^^^^ reference local42
//                                     ^^^^^^^^^^ reference local43

    EpoxyModel<?> model = viewHolder.getModel();
//  ^^^^^^^^^^ reference _root_/
//                ^^^^^ definition local44
//                        ^^^^^^^^^^ reference local46
//                                   ^^^^^^^^ reference getModel#
    if (!isTouchableModel(model)) {
//       ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#isTouchableModel().
//                        ^^^^^ reference local47
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 1:80
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "A model was dragged that is not a valid target: " + model.getClass());
//                                                             ^^^^^ reference local48
//                                                                   ^^^^^^^^ reference `<any>`#getClass#
    }

    //noinspection unchecked
    onModelMoved(fromPosition, toPosition, (T) model, viewHolder.itemView);
//  ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#onModelMoved().
//               ^^^^^^^^^^^^ reference local49
//                             ^^^^^^^^^^ reference local50
//                                          ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                                             ^^^^^ reference local51
//                                                    ^^^^^^^^^^ reference local52
//                                                               ^^^^^^^^ reference itemView#
    return true;
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onModelMoved(int fromPosition, int toPosition, T modelBeingMoved, View itemView) {
//            ^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onModelMoved().
//                             ^^^^^^^^^^^^ definition local53
//                                               ^^^^^^^^^^ definition local55
//                                                           ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                                                             ^^^^^^^^^^^^^^^ definition local57
//                                                                              ^^^^ reference _root_/
//                                                                                   ^^^^^^^^ definition local59

  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected void onSwiped(EpoxyViewHolder viewHolder, int direction) {
//               ^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onSwiped().
//                        ^^^^^^^^^^^^^^^ reference _root_/
//                                        ^^^^^^^^^^ definition local61
//                                                        ^^^^^^^^^ definition local63
    EpoxyModel<?> model = viewHolder.getModel();
//  ^^^^^^^^^^ reference _root_/
//                ^^^^^ definition local65
//                        ^^^^^^^^^^ reference local67
//                                   ^^^^^^^^ reference getModel#
    View view = viewHolder.itemView;
//  ^^^^ reference _root_/
//       ^^^^ definition local68
//              ^^^^^^^^^^ reference local70
//                         ^^^^^^^^ reference itemView#
    int position = viewHolder.getAdapterPosition();
//      ^^^^^^^^ definition local71
//                 ^^^^^^^^^^ reference local73
//                            ^^^^^^^^^^^^^^^^^^ reference getAdapterPosition#

    if (!isTouchableModel(model)) {
//       ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#isTouchableModel().
//                        ^^^^^ reference local74
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 1:79
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "A model was swiped that is not a valid target: " + model.getClass());
//                                                            ^^^^^ reference local75
//                                                                  ^^^^^^^^ reference `<any>`#getClass#
    }

    //noinspection unchecked
    onSwipeCompleted((T) model, view, position, direction);
//  ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#onSwipeCompleted().
//                    ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                       ^^^^^ reference local76
//                              ^^^^ reference local77
//                                    ^^^^^^^^ reference local78
//                                              ^^^^^^^^^ reference local79
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onSwipeCompleted(T model, View itemView, int position, int direction) {
//            ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onSwipeCompleted().
//                             ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                               ^^^^^ definition local80
//                                      ^^^^ reference _root_/
//                                           ^^^^^^^^ definition local82
//                                                         ^^^^^^^^ definition local84
//                                                                       ^^^^^^^^^ definition local86

  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected void onSelectedChanged(@Nullable EpoxyViewHolder viewHolder, int actionState) {
//               ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onSelectedChanged().
//                                  ^^^^^^^^ reference androidx/annotation/Nullable#
//                                           ^^^^^^^^^^^^^^^ reference _root_/
//                                                           ^^^^^^^^^^ definition local88
//                                                                           ^^^^^^^^^^^ definition local90
    super.onSelectedChanged(viewHolder, actionState);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^ reference onSelectedChanged#
//                          ^^^^^^^^^^ reference local92
//                                      ^^^^^^^^^^^ reference local93

    if (viewHolder != null) {
//      ^^^^^^^^^^ reference local94
      EpoxyModel<?> model = viewHolder.getModel();
//    ^^^^^^^^^^ reference _root_/
//                  ^^^^^ definition local95
//                          ^^^^^^^^^^ reference local97
//                                     ^^^^^^^^ reference getModel#
      if (!isTouchableModel(model)) {
//         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#isTouchableModel().
//                          ^^^^^ reference local98
        throw new IllegalStateException(
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 1:83
//                ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
            "A model was selected that is not a valid target: " + model.getClass());
//                                                                ^^^^^ reference local99
//                                                                      ^^^^^^^^ reference `<any>`#getClass#
      }

      markRecyclerViewHasSelection((RecyclerView) viewHolder.itemView.getParent());
//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#markRecyclerViewHasSelection().
//                                  ^^^^^^^^^^^^ reference _root_/
//                                                ^^^^^^^^^^ reference local100
//                                                           ^^^^^^^^ reference itemView#
//                                                                    ^^^^^^^^^ reference itemView#getParent#

      if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//        ^^^^^^^^^^^ reference local101
//                       ^^^^^^^^^^^^^^^ reference _root_/
//                                       ^^^^^^^^^^^^^^^^^^ reference ACTION_STATE_SWIPE#
        holderBeingSwiped = viewHolder;
//      ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingSwiped.
//                          ^^^^^^^^^^ reference local102
        //noinspection unchecked
        onSwipeStarted((T) model, viewHolder.itemView, viewHolder.getAdapterPosition());
//      ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#onSwipeStarted().
//                      ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                         ^^^^^ reference local103
//                                ^^^^^^^^^^ reference local104
//                                           ^^^^^^^^ reference itemView#
//                                                     ^^^^^^^^^^ reference local105
//                                                                ^^^^^^^^^^^^^^^^^^ reference getAdapterPosition#
      } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
//               ^^^^^^^^^^^ reference local106
//                              ^^^^^^^^^^^^^^^ reference _root_/
//                                              ^^^^^^^^^^^^^^^^^ reference ACTION_STATE_DRAG#
        holderBeingDragged = viewHolder;
//      ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingDragged.
//                           ^^^^^^^^^^ reference local107
        //noinspection unchecked
        onDragStarted((T) model, viewHolder.itemView, viewHolder.getAdapterPosition());
//      ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#onDragStarted().
//                     ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                        ^^^^^ reference local108
//                               ^^^^^^^^^^ reference local109
//                                          ^^^^^^^^ reference itemView#
//                                                    ^^^^^^^^^^ reference local110
//                                                               ^^^^^^^^^^^^^^^^^^ reference getAdapterPosition#
      }
    } else if (holderBeingDragged != null) {
//             ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingDragged.
      //noinspection unchecked
      onDragReleased((T) holderBeingDragged.getModel(), holderBeingDragged.itemView);
//    ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#onDragReleased().
//                    ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                       ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingDragged.
//                                          ^^^^^^^^ reference getModel#
//                                                      ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingDragged.
//                                                                         ^^^^^^^^ reference itemView#
      holderBeingDragged = null;
//    ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingDragged.
    } else if (holderBeingSwiped != null) {
//             ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingSwiped.
      //noinspection unchecked
      onSwipeReleased((T) holderBeingSwiped.getModel(), holderBeingSwiped.itemView);
//    ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#onSwipeReleased().
//                     ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                        ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingSwiped.
//                                          ^^^^^^^^ reference getModel#
//                                                      ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingSwiped.
//                                                                        ^^^^^^^^ reference itemView#
      holderBeingSwiped = null;
//    ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#holderBeingSwiped.
    }
  }

  private void markRecyclerViewHasSelection(RecyclerView recyclerView) {
//             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#markRecyclerViewHasSelection().
//                                          ^^^^^^^^^^^^ reference _root_/
//                                                       ^^^^^^^^^^^^ definition local111
    recyclerView.setTag(R.id.epoxy_touch_helper_selection_status, Boolean.TRUE);
//  ^^^^^^^^^^^^ reference local113
//               ^^^^^^ reference setTag#
//                      ^ reference R/
//                        ^^ reference R/id#
//                           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference R/id#epoxy_touch_helper_selection_status#
//                                                                ^^^^^^^ reference java/lang/Boolean#
//                                                                        ^^^^ reference java/lang/Boolean#TRUE.
  }

  private boolean recyclerViewHasSelection(RecyclerView recyclerView) {
//                ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#recyclerViewHasSelection().
//                                         ^^^^^^^^^^^^ reference _root_/
//                                                      ^^^^^^^^^^^^ definition local114
    return recyclerView.getTag(R.id.epoxy_touch_helper_selection_status) != null;
//         ^^^^^^^^^^^^ reference local116
//                      ^^^^^^ reference getTag#
//                             ^ reference R/
//                               ^^ reference R/id#
//                                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference R/id#epoxy_touch_helper_selection_status#
  }

  private void clearRecyclerViewSelectionMarker(RecyclerView recyclerView) {
//             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#clearRecyclerViewSelectionMarker().
//                                              ^^^^^^^^^^^^ reference _root_/
//                                                           ^^^^^^^^^^^^ definition local117
    recyclerView.setTag(R.id.epoxy_touch_helper_selection_status, null);
//  ^^^^^^^^^^^^ reference local119
//               ^^^^^^ reference setTag#
//                      ^ reference R/
//                        ^^ reference R/id#
//                           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference R/id#epoxy_touch_helper_selection_status#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onSwipeStarted(T model, View itemView, int adapterPosition) {
//            ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onSwipeStarted().
//                           ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                             ^^^^^ definition local120
//                                    ^^^^ reference _root_/
//                                         ^^^^^^^^ definition local122
//                                                       ^^^^^^^^^^^^^^^ definition local124

  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onSwipeReleased(T model, View itemView) {
//            ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onSwipeReleased().
//                            ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                              ^^^^^ definition local126
//                                     ^^^^ reference _root_/
//                                          ^^^^^^^^ definition local128

  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onDragStarted(T model, View itemView, int adapterPosition) {
//            ^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onDragStarted().
//                          ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                            ^^^^^ definition local130
//                                   ^^^^ reference _root_/
//                                        ^^^^^^^^ definition local132
//                                                      ^^^^^^^^^^^^^^^ definition local134

  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onDragReleased(T model, View itemView) {
//            ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onDragReleased().
//                           ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                             ^^^^^ definition local136
//                                    ^^^^ reference _root_/
//                                         ^^^^^^^^ definition local138

  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected void clearView(final RecyclerView recyclerView, EpoxyViewHolder viewHolder) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#clearView().
//                               ^^^^^^^^^^^^ reference _root_/
//                                            ^^^^^^^^^^^^ definition local140
//                                                          ^^^^^^^^^^^^^^^ reference _root_/
//                                                                          ^^^^^^^^^^ definition local142
    super.clearView(recyclerView, viewHolder);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference clearView#
//                  ^^^^^^^^^^^^ reference local144
//                                ^^^^^^^^^^ reference local145
    //noinspection unchecked
    clearView((T) viewHolder.getModel(), viewHolder.itemView);
//  ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#clearView().
//             ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                ^^^^^^^^^^ reference local146
//                           ^^^^^^^^ reference getModel#
//                                       ^^^^^^^^^^ reference local147
//                                                  ^^^^^^^^ reference itemView#

    // If multiple touch helpers are in use, one touch helper can pick up buffered touch inputs
    // immediately after another touch event finishes. This leads to things like a view being
    // selected for drag when another view finishes its swipe off animation. To prevent that we
    // keep the recyclerview marked as having an active selection for a brief period after a
    // touch event ends.
    recyclerView.postDelayed(new Runnable() {
//  ^^^^^^^^^^^^ reference local148
//               ^^^^^^^^^^^ reference postDelayed#
//                           ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#clearView().``#`<init>`(). 5:5
//                               ^^^^^^^^ reference java/lang/Runnable#
//                               ^^^^^^^^ reference java/lang/Runnable#
//                                          ^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#clearView().``#`<init>`(). 1:4
      @Override
//     ^^^^^^^^ reference java/lang/Override#
      public void run() {
//                ^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#clearView().``#run().
        clearRecyclerViewSelectionMarker(recyclerView);
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#clearRecyclerViewSelectionMarker().
//                                       ^^^^^^^^^^^^ reference local149
      }
    }, TOUCH_DEBOUNCE_MILLIS);
//     ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#TOUCH_DEBOUNCE_MILLIS.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void clearView(T model, View itemView) {
//            ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#clearView(+1).
//                      ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                        ^^^^^ definition local150
//                               ^^^^ reference _root_/
//                                    ^^^^^^^^ definition local152

  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected void onChildDraw(Canvas c, RecyclerView recyclerView, EpoxyViewHolder viewHolder,
//               ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onChildDraw().
//                           ^^^^^^ reference _root_/
//                                  ^ definition local154
//                                     ^^^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^^^^^^^ definition local156
//                                                                ^^^^^^^^^^^^^^^ reference _root_/
//                                                                                ^^^^^^^^^^ definition local158
      float dX, float dY, int actionState, boolean isCurrentlyActive) {
//          ^^ definition local160
//                    ^^ definition local162
//                            ^^^^^^^^^^^ definition local164
//                                                 ^^^^^^^^^^^^^^^^^ definition local166
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^ reference onChildDraw#
//                    ^ reference local168
//                       ^^^^^^^^^^^^ reference local169
//                                     ^^^^^^^^^^ reference local170
//                                                 ^^ reference local171
//                                                     ^^ reference local172
//                                                         ^^^^^^^^^^^ reference local173
//                                                                      ^^^^^^^^^^^^^^^^^ reference local174

    EpoxyModel<?> model = viewHolder.getModel();
//  ^^^^^^^^^^ reference _root_/
//                ^^^^^ definition local175
//                        ^^^^^^^^^^ reference local177
//                                   ^^^^^^^^ reference getModel#
    if (!isTouchableModel(model)) {
//       ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#isTouchableModel().
//                        ^^^^^ reference local178
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 1:81
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "A model was selected that is not a valid target: " + model.getClass());
//                                                              ^^^^^ reference local179
//                                                                    ^^^^^^^^ reference `<any>`#getClass#
    }

    View itemView = viewHolder.itemView;
//  ^^^^ reference _root_/
//       ^^^^^^^^ definition local180
//                  ^^^^^^^^^^ reference local182
//                             ^^^^^^^^ reference itemView#

    float swipeProgress;
//        ^^^^^^^^^^^^^ definition local183
    if (Math.abs(dX) > Math.abs(dY)) {
//      ^^^^ reference java/lang/Math#
//           ^^^ reference java/lang/Math#abs(+2).
//               ^^ reference local185
//                     ^^^^ reference java/lang/Math#
//                          ^^^ reference java/lang/Math#abs(+2).
//                              ^^ reference local186
      swipeProgress = dX / itemView.getWidth();
//    ^^^^^^^^^^^^^ reference local187
//                    ^^ reference local188
//                         ^^^^^^^^ reference local189
//                                  ^^^^^^^^ reference getWidth#
    } else {
      swipeProgress = dY / itemView.getHeight();
//    ^^^^^^^^^^^^^ reference local190
//                    ^^ reference local191
//                         ^^^^^^^^ reference local192
//                                  ^^^^^^^^^ reference getHeight#
    }

    // Clamp to 1/-1 in the case of side padding where the view can be swiped extra
    float clampedProgress = Math.max(-1f, Math.min(1f, swipeProgress));
//        ^^^^^^^^^^^^^^^ definition local193
//                          ^^^^ reference java/lang/Math#
//                               ^^^ reference java/lang/Math#max(+2).
//                                        ^^^^ reference java/lang/Math#
//                                             ^^^ reference java/lang/Math#min(+2).
//                                                     ^^^^^^^^^^^^^ reference local195

    //noinspection unchecked
    onSwipeProgressChanged((T) model, itemView, clampedProgress, c);
//  ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#onSwipeProgressChanged().
//                          ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                             ^^^^^ reference local196
//                                    ^^^^^^^^ reference local197
//                                              ^^^^^^^^^^^^^^^ reference local198
//                                                               ^ reference local199
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onSwipeProgressChanged(T model, View itemView, float swipeProgress,
//            ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelTouchCallback#onSwipeProgressChanged().
//                                   ^ reference com/airbnb/epoxy/EpoxyModelTouchCallback#[T]
//                                     ^^^^^ definition local200
//                                            ^^^^ reference _root_/
//                                                 ^^^^^^^^ definition local202
//                                                                 ^^^^^^^^^^^^^ definition local204
      Canvas canvas) {
//    ^^^^^^ reference _root_/
//           ^^^^^^ definition local206

  }
}

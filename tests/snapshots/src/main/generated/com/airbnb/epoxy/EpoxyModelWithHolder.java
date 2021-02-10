package com.airbnb.epoxy;

import android.view.ViewParent;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^^^^^^^ reference android/view/ViewParent#

import com.airbnb.epoxy.VisibilityState.Visibility;
//     ^^^ reference com/
//         ^^^^^^ reference com/airbnb/
//                ^^^^^ reference com/airbnb/epoxy/
//                      ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/VisibilityState/
//                                      ^^^^^^^^^^ reference com/airbnb/epoxy/VisibilityState/Visibility#

import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#

import androidx.annotation.FloatRange;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^^^ reference androidx/annotation/FloatRange#
import androidx.annotation.NonNull;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^ reference androidx/annotation/NonNull#
import androidx.annotation.Px;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^ reference androidx/annotation/Px#

/**
 * A version of {@link com.airbnb.epoxy.EpoxyModel} that allows you to use a view holder pattern
 * instead of a specific view when binding to your model.
 */
public abstract class EpoxyModelWithHolder<T extends EpoxyHolder> extends EpoxyModel<T> {
//              ^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#
//                                                   ^^^^^^^^^^^ reference _root_/
//                                                                        ^^^^^^^^^^ reference _root_/
//                                                                                   ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]

  public EpoxyModelWithHolder() {
//       ^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#`<init>`().
  }

  public EpoxyModelWithHolder(long id) {
//       ^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#`<init>`(+1).
//                                 ^^ definition local0
    super(id);
//        ^^ reference local2
  }

  /** This should return a new instance of your {@link com.airbnb.epoxy.EpoxyHolder} class. */
  protected abstract T createNewHolder(@NonNull ViewParent parent);
//                   ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                     ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#createNewHolder().
//                                      ^^^^^^^ reference androidx/annotation/NonNull#
//                                              ^^^^^^^^^^ reference _root_/
//                                                         ^^^^^^ definition local3

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void bind(@NonNull T holder) {
//            ^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#bind().
//                  ^^^^^^^ reference androidx/annotation/NonNull#
//                          ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                            ^^^^^^ definition local5
    super.bind(holder);
//  ^^^^^ reference _root_/
//        ^^^^ reference bind#
//             ^^^^^^ reference local7
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void bind(@NonNull T holder, @NonNull List<Object> payloads) {
//            ^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#bind(+1).
//                  ^^^^^^^ reference androidx/annotation/NonNull#
//                          ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                            ^^^^^^ definition local8
//                                     ^^^^^^^ reference androidx/annotation/NonNull#
//                                             ^^^^ reference java/util/List#
//                                                  ^^^^^^ reference java/lang/Object#
//                                                          ^^^^^^^^ definition local10
    super.bind(holder, payloads);
//  ^^^^^ reference _root_/
//        ^^^^ reference bind#
//             ^^^^^^ reference local12
//                     ^^^^^^^^ reference local13
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void bind(@NonNull T holder, @NonNull EpoxyModel<?> previouslyBoundModel) {
//            ^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#bind(+2).
//                  ^^^^^^^ reference androidx/annotation/NonNull#
//                          ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                            ^^^^^^ definition local14
//                                     ^^^^^^^ reference androidx/annotation/NonNull#
//                                             ^^^^^^^^^^ reference _root_/
//                                                           ^^^^^^^^^^^^^^^^^^^^ definition local16
    super.bind(holder, previouslyBoundModel);
//  ^^^^^ reference _root_/
//        ^^^^ reference bind#
//             ^^^^^^ reference local18
//                     ^^^^^^^^^^^^^^^^^^^^ reference local19
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void unbind(@NonNull T holder) {
//            ^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#unbind().
//                    ^^^^^^^ reference androidx/annotation/NonNull#
//                            ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                              ^^^^^^ definition local20
    super.unbind(holder);
//  ^^^^^ reference _root_/
//        ^^^^^^ reference unbind#
//               ^^^^^^ reference local22
  }


  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onVisibilityStateChanged(@Visibility int visibilityState, @NonNull T holder) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#onVisibilityStateChanged().
//                                      ^^^^^^^^^^ reference _root_/
//                                                     ^^^^^^^^^^^^^^^ definition local23
//                                                                       ^^^^^^^ reference androidx/annotation/NonNull#
//                                                                               ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                                                                                 ^^^^^^ definition local25
    super.onVisibilityStateChanged(visibilityState, holder);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^^^ reference onVisibilityStateChanged#
//                                 ^^^^^^^^^^^^^^^ reference local27
//                                                  ^^^^^^ reference local28
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onVisibilityChanged(
//            ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#onVisibilityChanged().
      @FloatRange(from = 0, to = 100) float percentVisibleHeight,
//     ^^^^^^^^^^ reference androidx/annotation/FloatRange#
//                ^^^^ reference androidx/annotation/FloatRange#from().
//                          ^^ reference androidx/annotation/FloatRange#to().
//                                          ^^^^^^^^^^^^^^^^^^^^ definition local29
      @FloatRange(from = 0, to = 100) float percentVisibleWidth,
//     ^^^^^^^^^^ reference androidx/annotation/FloatRange#
//                ^^^^ reference androidx/annotation/FloatRange#from().
//                          ^^ reference androidx/annotation/FloatRange#to().
//                                          ^^^^^^^^^^^^^^^^^^^ definition local31
      @Px int visibleHeight, @Px int visibleWidth,
//     ^^ reference androidx/annotation/Px#
//            ^^^^^^^^^^^^^ definition local33
//                            ^^ reference androidx/annotation/Px#
//                                   ^^^^^^^^^^^^ definition local35
      @NonNull T holder) {
//     ^^^^^^^ reference androidx/annotation/NonNull#
//             ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//               ^^^^^^ definition local37
    super.onVisibilityChanged(
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^ reference onVisibilityChanged#
        percentVisibleHeight, percentVisibleWidth,
//      ^^^^^^^^^^^^^^^^^^^^ reference local39
//                            ^^^^^^^^^^^^^^^^^^^ reference local40
        visibleHeight, visibleWidth,
//      ^^^^^^^^^^^^^ reference local41
//                     ^^^^^^^^^^^^ reference local42
        holder);
//      ^^^^^^ reference local43
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean onFailedToRecycleView(T holder) {
//               ^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#onFailedToRecycleView().
//                                     ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                                       ^^^^^^ definition local44
    return super.onFailedToRecycleView(holder);
//         ^^^^^ reference _root_/
//               ^^^^^^^^^^^^^^^^^^^^^ reference onFailedToRecycleView#
//                                     ^^^^^^ reference local46
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onViewAttachedToWindow(T holder) {
//            ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#onViewAttachedToWindow().
//                                   ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                                     ^^^^^^ definition local47
    super.onViewAttachedToWindow(holder);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^ reference onViewAttachedToWindow#
//                               ^^^^^^ reference local49
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onViewDetachedFromWindow(T holder) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyModelWithHolder#onViewDetachedFromWindow().
//                                     ^ reference com/airbnb/epoxy/EpoxyModelWithHolder#[T]
//                                       ^^^^^^ definition local50
    super.onViewDetachedFromWindow(holder);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^^^ reference onViewDetachedFromWindow#
//                                 ^^^^^^ reference local52
  }
}

package com.airbnb.epoxy;

import android.view.View;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^ reference android/view/View#
import android.view.ViewParent;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^^^^^^^ reference android/view/ViewParent#

import com.airbnb.epoxy.ViewHolderState.ViewState;
//     ^^^ reference com/
//         ^^^^^^ reference com/airbnb/
//                ^^^^^ reference com/airbnb/epoxy/
//                      ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState/
//                                      ^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState/ViewState#
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
import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#
import androidx.annotation.Px;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^ reference androidx/annotation/Px#
import androidx.recyclerview.widget.RecyclerView;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView#

@SuppressWarnings("WeakerAccess")
 ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
public class EpoxyViewHolder extends RecyclerView.ViewHolder {
//     ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#
//                                   ^^^^^^^^^^^^ reference RecyclerView/
//                                                ^^^^^^^^^^ reference RecyclerView/ViewHolder#
  @SuppressWarnings("rawtypes") private EpoxyModel epoxyModel;
   ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
//                                      ^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
  private List<Object> payloads;
//        ^^^^ reference java/util/List#
//             ^^^^^^ reference java/lang/Object#
//                     ^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#payloads.
  private EpoxyHolder epoxyHolder;
//        ^^^^^^^^^^^ reference _root_/
//                    ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#epoxyHolder.
  @Nullable ViewHolderState.ViewState initialViewState;
   ^^^^^^^^ reference androidx/annotation/Nullable#
//          ^^^^^^^^^^^^^^^ reference ViewHolderState/
//                          ^^^^^^^^^ reference ViewHolderState/ViewState#
//                                    ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#initialViewState.

  // Once the EpoxyHolder is created parent will be set to null.
  private ViewParent parent;
//        ^^^^^^^^^^ reference _root_/
//                   ^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#parent.

  public EpoxyViewHolder(ViewParent parent, View view, boolean saveInitialState) {
//       ^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#`<init>`().
//                       ^^^^^^^^^^ reference _root_/
//                                  ^^^^^^ definition local0
//                                          ^^^^ reference _root_/
//                                               ^^^^ definition local2
//                                                             ^^^^^^^^^^^^^^^^ definition local4
    super(view);
//        ^^^^ reference local6

    this.parent = parent;
//  ^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#this.
//       ^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#parent.
//                ^^^^^^ reference local7
    if (saveInitialState) {
//      ^^^^^^^^^^^^^^^^ reference local8
      // We save the initial state of the view when it is created so that we can reset this initial
      // state before a model is bound for the first time. Otherwise the view may carry over
      // state from a previously bound model.
      initialViewState = new ViewState();
//    ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#initialViewState.
//                           ^^^^^^^^^ reference _root_/
      initialViewState.save(itemView);
//    ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#initialViewState.
//                     ^^^^ reference ViewHolderState/ViewState#save#
//                          ^^^^^^^^ reference _root_/
    }
  }

  void restoreInitialViewState() {
//     ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#restoreInitialViewState().
    if (initialViewState != null) {
//      ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#initialViewState.
      initialViewState.restore(itemView);
//    ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#initialViewState.
//                     ^^^^^^^ reference ViewHolderState/ViewState#restore#
//                             ^^^^^^^^ reference _root_/
    }
  }

  public void bind(@SuppressWarnings("rawtypes") EpoxyModel model,
//            ^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#bind().
//                  ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
//                                               ^^^^^^^^^^ reference _root_/
//                                                          ^^^^^ definition local9
      @Nullable EpoxyModel<?> previouslyBoundModel, List<Object> payloads, int position) {
//     ^^^^^^^^ reference androidx/annotation/Nullable#
//              ^^^^^^^^^^ reference _root_/
//                            ^^^^^^^^^^^^^^^^^^^^ definition local11
//                                                  ^^^^ reference java/util/List#
//                                                       ^^^^^^ reference java/lang/Object#
//                                                               ^^^^^^^^ definition local13
//                                                                             ^^^^^^^^ definition local15
    this.payloads = payloads;
//  ^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#this.
//       ^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#payloads.
//                  ^^^^^^^^ reference local17

    if (epoxyHolder == null && model instanceof EpoxyModelWithHolder) {
//      ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyHolder.
//                             ^^^^^ reference local18
//                                              ^^^^^^^^^^^^^^^^^^^^ reference _root_/
      epoxyHolder = ((EpoxyModelWithHolder) model).createNewHolder(parent);
//    ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyHolder.
//                    ^^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                          ^^^^^ reference local19
//                                                 ^^^^^^^^^^^^^^^ reference createNewHolder#
//                                                                 ^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#parent.
      epoxyHolder.bindView(itemView);
//    ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyHolder.
//                ^^^^^^^^ reference bindView#
//                         ^^^^^^^^ reference _root_/
    }
    // Safe to set to null as it is only used for createNewHolder method
    parent = null;
//  ^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#parent.

    if (model instanceof GeneratedModel) {
//      ^^^^^ reference local20
//                       ^^^^^^^^^^^^^^ reference _root_/
      // The generated method will enforce that only a properly typed listener can be set
      //noinspection unchecked
      ((GeneratedModel) model).handlePreBind(this, objectToBind(), position);
//      ^^^^^^^^^^^^^^ reference _root_/
//                      ^^^^^ reference local21
//                             ^^^^^^^^^^^^^ reference handlePreBind#
//                                           ^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#this.
//                                                 ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#objectToBind().
//                                                                 ^^^^^^^^ reference local22
    }

    if (previouslyBoundModel != null) {
//      ^^^^^^^^^^^^^^^^^^^^ reference local23
      // noinspection unchecked
      model.bind(objectToBind(), previouslyBoundModel);
//    ^^^^^ reference local24
//          ^^^^ reference bind#
//               ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#objectToBind().
//                               ^^^^^^^^^^^^^^^^^^^^ reference local25
    } else if (payloads.isEmpty()) {
//             ^^^^^^^^ reference local26
//                      ^^^^^^^ reference java/util/List#isEmpty().
      // noinspection unchecked
      model.bind(objectToBind());
//    ^^^^^ reference local27
//          ^^^^ reference bind#
//               ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#objectToBind().
    } else {
      // noinspection unchecked
      model.bind(objectToBind(), payloads);
//    ^^^^^ reference local28
//          ^^^^ reference bind#
//               ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#objectToBind().
//                               ^^^^^^^^ reference local29
    }

    if (model instanceof GeneratedModel) {
//      ^^^^^ reference local30
//                       ^^^^^^^^^^^^^^ reference _root_/
      // The generated method will enforce that only a properly typed listener can be set
      //noinspection unchecked
      ((GeneratedModel) model).handlePostBind(objectToBind(), position);
//      ^^^^^^^^^^^^^^ reference _root_/
//                      ^^^^^ reference local31
//                             ^^^^^^^^^^^^^^ reference handlePostBind#
//                                            ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#objectToBind().
//                                                            ^^^^^^^^ reference local32
    }

    epoxyModel = model;
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
//               ^^^^^ reference local33
  }

  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  Object objectToBind() {
  ^^^^^^ reference java/lang/Object#
//       ^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#objectToBind().
    return epoxyHolder != null ? epoxyHolder : itemView;
//         ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyHolder.
//                               ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyHolder.
//                                             ^^^^^^^^ reference _root_/
  }

  public void unbind() {
//            ^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#unbind().
    assertBound();
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#assertBound().
    // noinspection unchecked
    epoxyModel.unbind(objectToBind());
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
//             ^^^^^^ reference unbind#
//                    ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#objectToBind().

    epoxyModel = null;
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
    payloads = null;
//  ^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#payloads.
  }

  public void visibilityStateChanged(@Visibility int visibilityState) {
//            ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#visibilityStateChanged().
//                                    ^^^^^^^^^^ reference _root_/
//                                                   ^^^^^^^^^^^^^^^ definition local34
    assertBound();
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#assertBound().
    // noinspection unchecked
    epoxyModel.onVisibilityStateChanged(visibilityState, objectToBind());
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
//             ^^^^^^^^^^^^^^^^^^^^^^^^ reference onVisibilityStateChanged#
//                                      ^^^^^^^^^^^^^^^ reference local36
//                                                       ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#objectToBind().
  }

  public void visibilityChanged(
//            ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#visibilityChanged().
      @FloatRange(from = 0.0f, to = 100.0f) float percentVisibleHeight,
//     ^^^^^^^^^^ reference androidx/annotation/FloatRange#
//                ^^^^ reference androidx/annotation/FloatRange#from().
//                             ^^ reference androidx/annotation/FloatRange#to().
//                                                ^^^^^^^^^^^^^^^^^^^^ definition local37
      @FloatRange(from = 0.0f, to = 100.0f) float percentVisibleWidth,
//     ^^^^^^^^^^ reference androidx/annotation/FloatRange#
//                ^^^^ reference androidx/annotation/FloatRange#from().
//                             ^^ reference androidx/annotation/FloatRange#to().
//                                                ^^^^^^^^^^^^^^^^^^^ definition local39
      @Px int visibleHeight,
//     ^^ reference androidx/annotation/Px#
//            ^^^^^^^^^^^^^ definition local41
      @Px int visibleWidth
//     ^^ reference androidx/annotation/Px#
//            ^^^^^^^^^^^^ definition local43
  ) {
    assertBound();
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#assertBound().
    // noinspection unchecked
    epoxyModel.onVisibilityChanged(percentVisibleHeight, percentVisibleWidth, visibleHeight,
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
//             ^^^^^^^^^^^^^^^^^^^ reference onVisibilityChanged#
//                                 ^^^^^^^^^^^^^^^^^^^^ reference local45
//                                                       ^^^^^^^^^^^^^^^^^^^ reference local46
//                                                                            ^^^^^^^^^^^^^ reference local47
        visibleWidth, objectToBind());
//      ^^^^^^^^^^^^ reference local48
//                    ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#objectToBind().
  }

  public List<Object> getPayloads() {
//       ^^^^ reference java/util/List#
//            ^^^^^^ reference java/lang/Object#
//                    ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#getPayloads().
    assertBound();
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#assertBound().
    return payloads;
//         ^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#payloads.
  }

  public EpoxyModel<?> getModel() {
//       ^^^^^^^^^^ reference _root_/
//                     ^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#getModel().
    assertBound();
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#assertBound().
    return epoxyModel;
//         ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
  }

  public EpoxyHolder getHolder() {
//       ^^^^^^^^^^^ reference _root_/
//                   ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#getHolder().
    assertBound();
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#assertBound().
    return epoxyHolder;
//         ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyHolder.
  }

  private void assertBound() {
//             ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#assertBound().
    if (epoxyModel == null) {
//      ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
      throw new IllegalStateException("This holder is not currently bound.");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
    }
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public String toString() {
//       ^^^^^^ reference java/lang/String#
//              ^^^^^^^^ definition com/airbnb/epoxy/EpoxyViewHolder#toString().
    return "EpoxyViewHolder{"
        + "epoxyModel=" + epoxyModel
//                        ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyViewHolder#epoxyModel.
        + ", view=" + itemView
//                    ^^^^^^^^ reference _root_/
        + ", super=" + super.toString()
//                     ^^^^^ reference _root_/
//                           ^^^^^^^^ reference toString#
        + '}';
  }
}

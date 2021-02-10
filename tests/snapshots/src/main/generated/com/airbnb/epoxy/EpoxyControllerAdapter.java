package com.airbnb.epoxy;

import android.os.Handler;
//     ^^^^^^^ reference android/
//             ^^ reference android/os/
//                ^^^^^^^ reference android/os/Handler#
import android.view.View;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^ reference android/view/View#

import com.airbnb.epoxy.AsyncEpoxyDiffer.ResultCallback;
//     ^^^ reference com/
//         ^^^^^^ reference com/airbnb/
//                ^^^^^ reference com/airbnb/epoxy/
//                      ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer/
//                                       ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer/ResultCallback#

import org.jetbrains.annotations.NotNull;
//     ^^^ reference org/
//         ^^^^^^^^^ reference org/jetbrains/
//                   ^^^^^^^^^^^ reference org/jetbrains/annotations/
//                               ^^^^^^^ reference org/jetbrains/annotations/NotNull#

import java.util.ArrayList;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^ reference java/util/ArrayList#
import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#

import androidx.annotation.NonNull;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^ reference androidx/annotation/NonNull#
import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#
import androidx.annotation.UiThread;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/UiThread#
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^ reference androidx/recyclerview/widget/DiffUtil/
//                                           ^^^^^^^^^^^^ reference androidx/recyclerview/widget/DiffUtil/ItemCallback#
import androidx.recyclerview.widget.RecyclerView;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView#

public final class EpoxyControllerAdapter extends BaseEpoxyAdapter implements ResultCallback {
//           ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#
//                                                ^^^^^^^^^^^^^^^^ reference _root_/
//                                                                            ^^^^^^^^^^^^^^ reference _root_/
  private final NotifyBlocker notifyBlocker = new NotifyBlocker();
//              ^^^^^^^^^^^^^ reference _root_/
//                            ^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#notifyBlocker.
//                                                ^^^^^^^^^^^^^ reference _root_/
  private final AsyncEpoxyDiffer differ;
//              ^^^^^^^^^^^^^^^^ reference _root_/
//                               ^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#differ.
  private final EpoxyController epoxyController;
//              ^^^^^^^^^^^^^^^ reference _root_/
//                              ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
  private int itemCount;
//            ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#itemCount.
  private final List<OnModelBuildFinishedListener> modelBuildListeners = new ArrayList<>();
//              ^^^^ reference java/util/List#
//                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#modelBuildListeners.
//                                                                       ^^^^^^^^^^^^^^^^^ reference java/util/ArrayList#`<init>`(+1).
//                                                                           ^^^^^^^^^ reference java/util/ArrayList#

  EpoxyControllerAdapter(@NonNull EpoxyController epoxyController, Handler diffingHandler) {
  ^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#`<init>`().
//                        ^^^^^^^ reference androidx/annotation/NonNull#
//                                ^^^^^^^^^^^^^^^ reference _root_/
//                                                ^^^^^^^^^^^^^^^ definition local0
//                                                                 ^^^^^^^ reference _root_/
//                                                                         ^^^^^^^^^^^^^^ definition local2
    this.epoxyController = epoxyController;
//  ^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#this.
//       ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                         ^^^^^^^^^^^^^^^ reference local4
    differ = new AsyncEpoxyDiffer(
//  ^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#differ.
//               ^^^^^^^^^^^^^^^^ reference _root_/
        diffingHandler,
//      ^^^^^^^^^^^^^^ reference local5
        this,
//      ^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#this.
        ITEM_CALLBACK
//      ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#ITEM_CALLBACK.
    );
    registerAdapterDataObserver(notifyBlocker);
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#registerAdapterDataObserver#
//                              ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyBlocker.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected void onExceptionSwallowed(@NonNull RuntimeException exception) {
//               ^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#onExceptionSwallowed().
//                                     ^^^^^^^ reference androidx/annotation/NonNull#
//                                             ^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#
//                                                              ^^^^^^^^^ definition local6
    epoxyController.onExceptionSwallowed(exception);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^^^^^^^^^ reference onExceptionSwallowed#
//                                       ^^^^^^^^^ reference local8
  }

  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  @Override
   ^^^^^^^^ reference java/lang/Override#
  List<? extends EpoxyModel<?>> getCurrentModels() {
  ^^^^ reference java/util/List#
//               ^^^^^^^^^^ reference _root_/
//                              ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().
    return differ.getCurrentList();
//         ^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#differ.
//                ^^^^^^^^^^^^^^ reference getCurrentList#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public int getItemCount() {
//           ^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#getItemCount().
    // RecyclerView calls this A LOT. The base class implementation does
    // getCurrentModels().size() which adds some overhead because of the method calls.
    // We can easily memoize this, which seems to help when there are lots of models.
    return itemCount;
//         ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#itemCount.
  }

  /** This is set from whatever thread model building happened on, so must be thread safe. */
  void setModels(@NonNull ControllerModelList models) {
//     ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#setModels().
//                ^^^^^^^ reference androidx/annotation/NonNull#
//                        ^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                            ^^^^^^ definition local9
    // If debug model validations are on then we should help detect the error case where models
    // were incorrectly mutated once they were added. That check is also done before and after
    // bind, but there is no other check after that to see if a model is incorrectly
    // mutated after being bound.
    // If a data class inside a model is mutated, then when models are rebuilt the differ
    // will still recognize the old and new models as equal, even though the old model was changed.
    // To help catch that error case we check for mutations here, before running the differ.
    //
    // https://github.com/airbnb/epoxy/issues/805
    List<? extends EpoxyModel<?>> currentModels = getCurrentModels();
//  ^^^^ reference java/util/List#
//                 ^^^^^^^^^^ reference _root_/
//                                ^^^^^^^^^^^^^ definition local11
//                                                ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().
    if (!currentModels.isEmpty() && currentModels.get(0).isDebugValidationEnabled()) {
//       ^^^^^^^^^^^^^ reference local13
//                     ^^^^^^^ reference java/util/List#isEmpty().
//                                  ^^^^^^^^^^^^^ reference local14
//                                                ^^^ reference java/util/List#get().
//                                                       ^^^^^^^^^^^^^^^^^^^^^^^^ reference `<any>`#isDebugValidationEnabled#
      for (int i = 0; i < currentModels.size(); i++) {
//             ^ definition local15
//                    ^ reference local17
//                        ^^^^^^^^^^^^^ reference local18
//                                      ^^^^ reference java/util/List#size().
//                                              ^ reference local19
        EpoxyModel<?> model = currentModels.get(i);
//      ^^^^^^^^^^ reference _root_/
//                    ^^^^^ definition local20
//                            ^^^^^^^^^^^^^ reference local22
//                                          ^^^ reference java/util/List#get().
//                                              ^ reference local23
        model.validateStateHasNotChangedSinceAdded(
//      ^^^^^ reference local24
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference `<any>`#validateStateHasNotChangedSinceAdded#
            "The model was changed between being bound and when models were rebuilt",
            i
//          ^ reference local25
        );
      }
    }

    differ.submitList(models);
//  ^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#differ.
//         ^^^^^^^^^^ reference submitList#
//                    ^^^^^^ reference local26
  }

  /**
   * @return True if a diff operation is in progress.
   */
  public boolean isDiffInProgress() {
//               ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#isDiffInProgress().
    return differ.isDiffInProgress();
//         ^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#differ.
//                ^^^^^^^^^^^^^^^^ reference isDiffInProgress#
  }

  // Called on diff results from the differ
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onResult(@NonNull DiffResult result) {
//            ^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#onResult().
//                      ^^^^^^^ reference androidx/annotation/NonNull#
//                              ^^^^^^^^^^ reference _root_/
//                                         ^^^^^^ definition local27
    itemCount = result.newModels.size();
//  ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#itemCount.
//              ^^^^^^ reference local29
//                     ^^^^^^^^^ reference newModels#
//                               ^^^^ reference newModels#size#
    notifyBlocker.allowChanges();
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyBlocker.
//                ^^^^^^^^^^^^ reference allowChanges#
    result.dispatchTo(this);
//  ^^^^^^ reference local30
//         ^^^^^^^^^^ reference dispatchTo#
//                    ^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#this.
    notifyBlocker.blockChanges();
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyBlocker.
//                ^^^^^^^^^^^^ reference blockChanges#

    for (int i = modelBuildListeners.size() - 1; i >= 0; i--) {
//           ^ definition local31
//               ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#modelBuildListeners.
//                                   ^^^^ reference java/util/List#size().
//                                               ^ reference local33
//                                                       ^ reference local34
      modelBuildListeners.get(i).onModelBuildFinished(result);
//    ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#modelBuildListeners.
//                        ^^^ reference java/util/List#get().
//                            ^ reference local35
//                               ^^^^^^^^^^^^^^^^^^^^ reference onModelBuildFinished#
//                                                    ^^^^^^ reference local36
    }
  }

  public void addModelBuildListener(OnModelBuildFinishedListener listener) {
//            ^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#addModelBuildListener().
//                                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                                               ^^^^^^^^ definition local37
    modelBuildListeners.add(listener);
//  ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#modelBuildListeners.
//                      ^^^ reference java/util/List#add().
//                          ^^^^^^^^ reference local39
  }

  public void removeModelBuildListener(OnModelBuildFinishedListener listener) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#removeModelBuildListener().
//                                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                                                  ^^^^^^^^ definition local40
    modelBuildListeners.remove(listener);
//  ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#modelBuildListeners.
//                      ^^^^^^ reference java/util/List#remove().
//                             ^^^^^^^^ reference local42
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  boolean diffPayloadsEnabled() {
//        ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#diffPayloadsEnabled().
    return true;
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#onAttachedToRecyclerView().
//                                      ^^^^^^^ reference androidx/annotation/NonNull#
//                                              ^^^^^^^^^^^^ reference _root_/
//                                                           ^^^^^^^^^^^^ definition local43
    super.onAttachedToRecyclerView(recyclerView);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^^^ reference onAttachedToRecyclerView#
//                                 ^^^^^^^^^^^^ reference local45
    epoxyController.onAttachedToRecyclerViewInternal(recyclerView);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference onAttachedToRecyclerViewInternal#
//                                                   ^^^^^^^^^^^^ reference local46
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#onDetachedFromRecyclerView().
//                                        ^^^^^^^ reference androidx/annotation/NonNull#
//                                                ^^^^^^^^^^^^ reference _root_/
//                                                             ^^^^^^^^^^^^ definition local47
    super.onDetachedFromRecyclerView(recyclerView);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference onDetachedFromRecyclerView#
//                                   ^^^^^^^^^^^^ reference local49
    epoxyController.onDetachedFromRecyclerViewInternal(recyclerView);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference onDetachedFromRecyclerViewInternal#
//                                                     ^^^^^^^^^^^^ reference local50
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onViewAttachedToWindow(@NonNull EpoxyViewHolder holder) {
//            ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#onViewAttachedToWindow().
//                                    ^^^^^^^ reference androidx/annotation/NonNull#
//                                            ^^^^^^^^^^^^^^^ reference _root_/
//                                                            ^^^^^^ definition local51
    super.onViewAttachedToWindow(holder);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^ reference onViewAttachedToWindow#
//                               ^^^^^^ reference local53
    epoxyController.onViewAttachedToWindow(holder, holder.getModel());
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^^^^^^^^^^^ reference onViewAttachedToWindow#
//                                         ^^^^^^ reference local54
//                                                 ^^^^^^ reference local55
//                                                        ^^^^^^^^ reference getModel#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onViewDetachedFromWindow(@NonNull EpoxyViewHolder holder) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#onViewDetachedFromWindow().
//                                      ^^^^^^^ reference androidx/annotation/NonNull#
//                                              ^^^^^^^^^^^^^^^ reference _root_/
//                                                              ^^^^^^ definition local56
    super.onViewDetachedFromWindow(holder);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^^^ reference onViewDetachedFromWindow#
//                                 ^^^^^^ reference local58
    epoxyController.onViewDetachedFromWindow(holder, holder.getModel());
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^^^^^^^^^^^^^ reference onViewDetachedFromWindow#
//                                           ^^^^^^ reference local59
//                                                   ^^^^^^ reference local60
//                                                          ^^^^^^^^ reference getModel#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected void onModelBound(@NonNull EpoxyViewHolder holder, @NonNull EpoxyModel<?> model,
//               ^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#onModelBound().
//                             ^^^^^^^ reference androidx/annotation/NonNull#
//                                     ^^^^^^^^^^^^^^^ reference _root_/
//                                                     ^^^^^^ definition local61
//                                                              ^^^^^^^ reference androidx/annotation/NonNull#
//                                                                      ^^^^^^^^^^ reference _root_/
//                                                                                    ^^^^^ definition local63
      int position, @Nullable EpoxyModel<?> previouslyBoundModel) {
//        ^^^^^^^^ definition local65
//                   ^^^^^^^^ reference androidx/annotation/Nullable#
//                            ^^^^^^^^^^ reference _root_/
//                                          ^^^^^^^^^^^^^^^^^^^^ definition local67
    epoxyController.onModelBound(holder, model, position, previouslyBoundModel);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^ reference onModelBound#
//                               ^^^^^^ reference local69
//                                       ^^^^^ reference local70
//                                              ^^^^^^^^ reference local71
//                                                        ^^^^^^^^^^^^^^^^^^^^ reference local72
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected void onModelUnbound(@NonNull EpoxyViewHolder holder, @NonNull EpoxyModel<?> model) {
//               ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#onModelUnbound().
//                               ^^^^^^^ reference androidx/annotation/NonNull#
//                                       ^^^^^^^^^^^^^^^ reference _root_/
//                                                       ^^^^^^ definition local73
//                                                                ^^^^^^^ reference androidx/annotation/NonNull#
//                                                                        ^^^^^^^^^^ reference _root_/
//                                                                                      ^^^^^ definition local75
    epoxyController.onModelUnbound(holder, model);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^^^ reference onModelUnbound#
//                                 ^^^^^^ reference local77
//                                         ^^^^^ reference local78
  }

  /** Get an unmodifiable copy of the current models set on the adapter. */
  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  public List<EpoxyModel<?>> getCopyOfModels() {
//       ^^^^ reference java/util/List#
//            ^^^^^^^^^^ reference _root_/
//                           ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#getCopyOfModels().
    //noinspection unchecked
    return (List<EpoxyModel<?>>) getCurrentModels();
//          ^^^^ reference java/util/List#
//               ^^^^^^^^^^ reference _root_/
//                               ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().
  }

  /**
   * @throws IndexOutOfBoundsException If the given position is out of range of the current model
   *                                   list.
   */
  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  public EpoxyModel<?> getModelAtPosition(int position) {
//       ^^^^^^^^^^ reference _root_/
//                     ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#getModelAtPosition().
//                                            ^^^^^^^^ definition local79
    return getCurrentModels().get(position);
//         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().
//                            ^^^ reference java/util/List#get().
//                                ^^^^^^^^ reference local81
  }

  /**
   * Searches the current model list for the model with the given id. Returns the matching model if
   * one is found, otherwise null is returned.
   */
  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  public EpoxyModel<?> getModelById(long id) {
//       ^^^^^^^^^^ reference _root_/
//                     ^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#getModelById().
//                                       ^^ definition local82
    for (EpoxyModel<?> model : getCurrentModels()) {
//       ^^^^^^^^^^ reference _root_/
//                     ^^^^^ definition local84
//                             ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().
      if (model.id() == id) {
//        ^^^^^ reference local86
//              ^^ reference `<any>`#id#
//                      ^^ reference local87
        return model;
//             ^^^^^ reference local88
      }
    }

    return null;
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public int getModelPosition(@NonNull EpoxyModel<?> targetModel) {
//           ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#getModelPosition().
//                             ^^^^^^^ reference androidx/annotation/NonNull#
//                                     ^^^^^^^^^^ reference _root_/
//                                                   ^^^^^^^^^^^ definition local89
    int size = getCurrentModels().size();
//      ^^^^ definition local91
//             ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().
//                                ^^^^ reference java/util/List#size().
    for (int i = 0; i < size; i++) {
//           ^ definition local93
//                  ^ reference local95
//                      ^^^^ reference local96
//                            ^ reference local97
      EpoxyModel<?> model = getCurrentModels().get(i);
//    ^^^^^^^^^^ reference _root_/
//                  ^^^^^ definition local98
//                          ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().
//                                             ^^^ reference java/util/List#get().
//                                                 ^ reference local100
      if (model.id() == targetModel.id()) {
//        ^^^^^ reference local101
//              ^^ reference `<any>`#id#
//                      ^^^^^^^^^^^ reference local102
//                                  ^^ reference `<any>`#id#
        return i;
//             ^ reference local103
      }
    }

    return -1;
  }

  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public BoundViewHolders getBoundViewHolders() {
//       ^^^^^^^^^^^^^^^^ reference _root_/
//                        ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#getBoundViewHolders().
    return super.getBoundViewHolders();
//         ^^^^^ reference _root_/
//               ^^^^^^^^^^^^^^^^^^^ reference getBoundViewHolders#
  }

  @UiThread
   ^^^^^^^^ reference androidx/annotation/UiThread#
  void moveModel(int fromPosition, int toPosition) {
//     ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#moveModel().
//                   ^^^^^^^^^^^^ definition local104
//                                     ^^^^^^^^^^ definition local106
    ArrayList<EpoxyModel<?>> updatedList = new ArrayList<>(getCurrentModels());
//  ^^^^^^^^^ reference java/util/ArrayList#
//            ^^^^^^^^^^ reference _root_/
//                           ^^^^^^^^^^^ definition local108
//                                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ArrayList#`<init>`(+2).
//                                             ^^^^^^^^^ reference java/util/ArrayList#
//                                                         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().

    updatedList.add(toPosition, updatedList.remove(fromPosition));
//  ^^^^^^^^^^^ reference local110
//              ^^^ reference java/util/ArrayList#add(+1).
//                  ^^^^^^^^^^ reference local111
//                              ^^^^^^^^^^^ reference local112
//                                          ^^^^^^ reference java/util/ArrayList#remove().
//                                                 ^^^^^^^^^^^^ reference local113
    notifyBlocker.allowChanges();
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyBlocker.
//                ^^^^^^^^^^^^ reference allowChanges#
    notifyItemMoved(fromPosition, toPosition);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyItemMoved#
//                  ^^^^^^^^^^^^ reference local114
//                                ^^^^^^^^^^ reference local115
    notifyBlocker.blockChanges();
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyBlocker.
//                ^^^^^^^^^^^^ reference blockChanges#

    boolean interruptedDiff = differ.forceListOverride(updatedList);
//          ^^^^^^^^^^^^^^^ definition local116
//                            ^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#differ.
//                                   ^^^^^^^^^^^^^^^^^ reference forceListOverride#
//                                                     ^^^^^^^^^^^ reference local118

    if (interruptedDiff) {
//      ^^^^^^^^^^^^^^^ reference local119
      // The move interrupted a model rebuild/diff that was in progress,
      // so models may be out of date and we should force them to rebuilt
      epoxyController.requestModelBuild();
//    ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                    ^^^^^^^^^^^^^^^^^ reference requestModelBuild#
    }
  }

  @UiThread
   ^^^^^^^^ reference androidx/annotation/UiThread#
  void notifyModelChanged(int position) {
//     ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#notifyModelChanged().
//                            ^^^^^^^^ definition local120
    ArrayList<EpoxyModel<?>> updatedList = new ArrayList<>(getCurrentModels());
//  ^^^^^^^^^ reference java/util/ArrayList#
//            ^^^^^^^^^^ reference _root_/
//                           ^^^^^^^^^^^ definition local122
//                                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ArrayList#`<init>`(+2).
//                                             ^^^^^^^^^ reference java/util/ArrayList#
//                                                         ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#getCurrentModels().

    notifyBlocker.allowChanges();
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyBlocker.
//                ^^^^^^^^^^^^ reference allowChanges#
    notifyItemChanged(position);
//  ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyItemChanged#
//                    ^^^^^^^^ reference local124
    notifyBlocker.blockChanges();
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#notifyBlocker.
//                ^^^^^^^^^^^^ reference blockChanges#

    boolean interruptedDiff = differ.forceListOverride(updatedList);
//          ^^^^^^^^^^^^^^^ definition local125
//                            ^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#differ.
//                                   ^^^^^^^^^^^^^^^^^ reference forceListOverride#
//                                                     ^^^^^^^^^^^ reference local127

    if (interruptedDiff) {
//      ^^^^^^^^^^^^^^^ reference local128
      // The move interrupted a model rebuild/diff that was in progress,
      // so models may be out of date and we should force them to rebuilt
      epoxyController.requestModelBuild();
//    ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                    ^^^^^^^^^^^^^^^^^ reference requestModelBuild#
    }
  }

  private static final ItemCallback<EpoxyModel<?>> ITEM_CALLBACK =
//                     ^^^^^^^^^^^^ reference _root_/
//                                  ^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#ITEM_CALLBACK.
      new ItemCallback<EpoxyModel<?>>() {
//        ^^^^^^^^^^^^ reference _root_/
//                     ^^^^^^^^^^ reference _root_/
        @Override
        public boolean areItemsTheSame(EpoxyModel<?> oldItem, EpoxyModel<?> newItem) {
          return oldItem.id() == newItem.id();
        }

        @Override
        public boolean areContentsTheSame(EpoxyModel<?> oldItem, EpoxyModel<?> newItem) {
          return oldItem.equals(newItem);
        }

        @Override
        public Object getChangePayload(EpoxyModel<?> oldItem, EpoxyModel<?> newItem) {
          return new DiffPayload(oldItem);
        }
      };

  /**
   * Delegates the callbacks received in the adapter
   * to the controller.
   */
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean isStickyHeader(int position) {
//               ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#isStickyHeader().
//                                  ^^^^^^^^ definition local129
    return epoxyController.isStickyHeader(position);
//         ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                         ^^^^^^^^^^^^^^ reference isStickyHeader#
//                                        ^^^^^^^^ reference local131
  }

  /**
   * Delegates the callbacks received in the adapter
   * to the controller.
   */
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void setupStickyHeaderView(@NotNull View stickyHeader) {
//            ^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#setupStickyHeaderView().
//                                   ^^^^^^^ reference org/jetbrains/annotations/NotNull#
//                                           ^^^^ reference _root_/
//                                                ^^^^^^^^^^^^ definition local132
    epoxyController.setupStickyHeaderView(stickyHeader);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^^^^^^^^^^ reference setupStickyHeaderView#
//                                        ^^^^^^^^^^^^ reference local134
  }

  /**
   * Delegates the callbacks received in the adapter
   * to the controller.
   */
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void teardownStickyHeaderView(@NotNull View stickyHeader) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyControllerAdapter#teardownStickyHeaderView().
//                                      ^^^^^^^ reference org/jetbrains/annotations/NotNull#
//                                              ^^^^ reference _root_/
//                                                   ^^^^^^^^^^^^ definition local135
    epoxyController.teardownStickyHeaderView(stickyHeader);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyControllerAdapter#epoxyController.
//                  ^^^^^^^^^^^^^^^^^^^^^^^^ reference teardownStickyHeaderView#
//                                           ^^^^^^^^^^^^ reference local137
  }
}

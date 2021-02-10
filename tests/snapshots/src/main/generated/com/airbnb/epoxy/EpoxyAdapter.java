
package com.airbnb.epoxy;

import java.util.Arrays;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^ reference java/util/Arrays#
import java.util.Collection;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^ reference java/util/Collection#
import java.util.Collections;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^^ reference java/util/Collections#
import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#

/**
 * Allows you to easily combine different view types in the same adapter, and handles view holder
 * creation, binding, and ids for you. Subclasses just need to add their desired {@link EpoxyModel}
 * objects and the rest is done automatically.
 * <p/>
 * {@link androidx.recyclerview.widget.RecyclerView.Adapter#setHasStableIds(boolean)} is set to true
 * by default, since {@link EpoxyModel} makes it easy to support unique ids. If you don't want to
 * support this then disable it in your base class (not recommended).
 */
@SuppressWarnings("WeakerAccess")
 ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
public abstract class EpoxyAdapter extends BaseEpoxyAdapter {
//              ^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#`<init>`().
//              ^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#
//                                         ^^^^^^^^^^^^^^^^ reference _root_/
  private final HiddenEpoxyModel hiddenModel = new HiddenEpoxyModel();
//              ^^^^^^^^^^^^^^^^ reference _root_/
//                               ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#hiddenModel.
//                                                 ^^^^^^^^^^^^^^^^ reference _root_/

  /**
   * Subclasses should modify this list as necessary with the models they want to show. Subclasses
   * are responsible for notifying data changes whenever this list is changed.
   */
  protected final List<EpoxyModel<?>> models = new ModelList();
//                ^^^^ reference java/util/List#
//                     ^^^^^^^^^^ reference _root_/
//                                    ^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#models.
//                                                 ^^^^^^^^^ reference _root_/
  private DiffHelper diffHelper;
//        ^^^^^^^^^^ reference _root_/
//                   ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#diffHelper.

  @Override
   ^^^^^^^^ reference java/lang/Override#
  List<EpoxyModel<?>> getCurrentModels() {
  ^^^^ reference java/util/List#
//     ^^^^^^^^^^ reference _root_/
//                    ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#getCurrentModels().
    return models;
//         ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
  }

  /**
   * Enables support for automatically notifying model changes via {@link #notifyModelsChanged()}.
   * If used, this should be called in the constructor, before any models are changed.
   *
   * @see #notifyModelsChanged()
   */
  protected void enableDiffing() {
//               ^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#enableDiffing().
    if (diffHelper != null) {
//      ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#diffHelper.
      throw new IllegalStateException("Diffing was already enabled");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
    }

    if (!models.isEmpty()) {
//       ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//              ^^^^^^^ reference java/util/List#isEmpty().
      throw new IllegalStateException("You must enable diffing before modifying models");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
    }

    if (!hasStableIds()) {
//       ^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#hasStableIds#
      throw new IllegalStateException("You must have stable ids to use diffing");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
    }

    diffHelper = new DiffHelper(this, false);
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#diffHelper.
//                   ^^^^^^^^^^ reference _root_/
//                              ^^^^ reference com/airbnb/epoxy/EpoxyAdapter#this.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  EpoxyModel<?> getModelForPosition(int position) {
  ^^^^^^^^^^ reference _root_/
//              ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#getModelForPosition().
//                                      ^^^^^^^^ definition local0
    EpoxyModel<?> model = models.get(position);
//  ^^^^^^^^^^ reference _root_/
//                ^^^^^ definition local2
//                        ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                               ^^^ reference java/util/List#get().
//                                   ^^^^^^^^ reference local4
    return model.isShown() ? model : hiddenModel;
//         ^^^^^ reference local5
//               ^^^^^^^ reference `<any>`#isShown#
//                           ^^^^^ reference local6
//                                   ^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#hiddenModel.
  }

  /**
   * Intelligently notify item changes by comparing the current {@link #models} list against the
   * previous so you don't have to micromanage notification calls yourself. This may be
   * prohibitively slow for large model lists (in the hundreds), in which case consider doing
   * notification calls yourself. If you use this, all your view models must implement {@link
   * EpoxyModel#hashCode()} and {@link EpoxyModel#equals(Object)} to completely identify their
   * state, so that changes to a model's content can be detected. Before using this you must enable
   * it with {@link #enableDiffing()}, since keeping track of the model state adds extra computation
   * time to all other data change notifications.
   *
   * @see #enableDiffing()
   */

  protected void notifyModelsChanged() {
//               ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#notifyModelsChanged().
    if (diffHelper == null) {
//      ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#diffHelper.
      throw new IllegalStateException("You must enable diffing before notifying models changed");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
    }

    diffHelper.notifyModelChanges();
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#diffHelper.
//             ^^^^^^^^^^^^^^^^^^ reference notifyModelChanges#
  }

  /**
   * Notify that the given model has had its data changed. It should only be called if the model
   * retained the same position.
   */
  protected void notifyModelChanged(EpoxyModel<?> model) {
//               ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#notifyModelChanged().
//                                  ^^^^^^^^^^ reference _root_/
//                                                ^^^^^ definition local7
    notifyModelChanged(model, null);
//  ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyModelChanged(+1).
//                     ^^^^^ reference local9
  }

  /**
   * Notify that the given model has had its data changed. It should only be called if the model
   * retained the same position.
   */
  protected void notifyModelChanged(EpoxyModel<?> model, @Nullable Object payload) {
//               ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#notifyModelChanged(+1).
//                                  ^^^^^^^^^^ reference _root_/
//                                                ^^^^^ definition local10
//                                                        ^^^^^^^^ reference androidx/annotation/Nullable#
//                                                                 ^^^^^^ reference java/lang/Object#
//                                                                        ^^^^^^^ definition local12
    int index = getModelPosition(model);
//      ^^^^^ definition local14
//              ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#getModelPosition#
//                               ^^^^^ reference local16
    if (index != -1) {
//      ^^^^^ reference local17
      notifyItemChanged(index, payload);
//    ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemChanged#
//                      ^^^^^ reference local18
//                             ^^^^^^^ reference local19
    }
  }

  /**
   * Adds the model to the end of the {@link #models} list and notifies that the item was inserted.
   */
  protected void addModel(EpoxyModel<?> modelToAdd) {
//               ^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#addModel().
//                        ^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^^^^ definition local20
    int initialSize = models.size();
//      ^^^^^^^^^^^ definition local22
//                    ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                           ^^^^ reference java/util/List#size().

    pauseModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
    models.add(modelToAdd);
//  ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//         ^^^ reference java/util/List#add().
//             ^^^^^^^^^^ reference local24
    resumeModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().

    notifyItemRangeInserted(initialSize, 1);
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemRangeInserted#
//                          ^^^^^^^^^^^ reference local25
  }

  /**
   * Adds the models to the end of the {@link #models} list and notifies that the items were
   * inserted.
   */
  protected void addModels(EpoxyModel<?>... modelsToAdd) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#addModels().
//                         ^^^^^^^^^^ reference _root_/
//                                          ^^^^^^^^^^^ definition local26
    int initialSize = models.size();
//      ^^^^^^^^^^^ definition local28
//                    ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                           ^^^^ reference java/util/List#size().
    int numModelsToAdd = modelsToAdd.length;
//      ^^^^^^^^^^^^^^ definition local30
//                       ^^^^^^^^^^^ reference local32
//                                   ^^^^^^ reference length.

    ((ModelList) models).ensureCapacity(initialSize + numModelsToAdd);
//    ^^^^^^^^^ reference _root_/
//               ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                       ^^^^^^^^^^^^^^ reference ensureCapacity#
//                                      ^^^^^^^^^^^ reference local33
//                                                    ^^^^^^^^^^^^^^ reference local34

    pauseModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
    Collections.addAll(models, modelsToAdd);
//  ^^^^^^^^^^^ reference java/util/Collections#
//              ^^^^^^ reference java/util/Collections#addAll().
//                     ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                             ^^^^^^^^^^^ reference local35
    resumeModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().

    notifyItemRangeInserted(initialSize, numModelsToAdd);
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemRangeInserted#
//                          ^^^^^^^^^^^ reference local36
//                                       ^^^^^^^^^^^^^^ reference local37
  }

  /**
   * Adds the models to the end of the {@link #models} list and notifies that the items were
   * inserted.
   */
  protected void addModels(Collection<? extends EpoxyModel<?>> modelsToAdd) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#addModels(+1).
//                         ^^^^^^^^^^ reference java/util/Collection#
//                                              ^^^^^^^^^^ reference _root_/
//                                                             ^^^^^^^^^^^ definition local38
    int initialSize = models.size();
//      ^^^^^^^^^^^ definition local40
//                    ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                           ^^^^ reference java/util/List#size().

    pauseModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
    models.addAll(modelsToAdd);
//  ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//         ^^^^^^ reference java/util/List#addAll().
//                ^^^^^^^^^^^ reference local42
    resumeModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().

    notifyItemRangeInserted(initialSize, modelsToAdd.size());
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemRangeInserted#
//                          ^^^^^^^^^^^ reference local43
//                                       ^^^^^^^^^^^ reference local44
//                                                   ^^^^ reference java/util/Collection#size().
  }

  /**
   * Inserts the given model before the other in the {@link #models} list, and notifies that the
   * item was inserted.
   */
  protected void insertModelBefore(EpoxyModel<?> modelToInsert, EpoxyModel<?> modelToInsertBefore) {
//               ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#insertModelBefore().
//                                 ^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^^^^ definition local45
//                                                              ^^^^^^^^^^ reference _root_/
//                                                                            ^^^^^^^^^^^^^^^^^^^ definition local47
    int targetIndex = getModelPosition(modelToInsertBefore);
//      ^^^^^^^^^^^ definition local49
//                    ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#getModelPosition#
//                                     ^^^^^^^^^^^^^^^^^^^ reference local51
    if (targetIndex == -1) {
//      ^^^^^^^^^^^ reference local52
      throw new IllegalStateException("Model is not added: " + modelToInsertBefore);
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
//                                                             ^^^^^^^^^^^^^^^^^^^ reference local53
    }

    pauseModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
    models.add(targetIndex, modelToInsert);
//  ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//         ^^^ reference java/util/List#add(+1).
//             ^^^^^^^^^^^ reference local54
//                          ^^^^^^^^^^^^^ reference local55
    resumeModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().

    notifyItemInserted(targetIndex);
//  ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemInserted#
//                     ^^^^^^^^^^^ reference local56
  }

  /**
   * Inserts the given model after the other in the {@link #models} list, and notifies that the item
   * was inserted.
   */
  protected void insertModelAfter(EpoxyModel<?> modelToInsert, EpoxyModel<?> modelToInsertAfter) {
//               ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#insertModelAfter().
//                                ^^^^^^^^^^ reference _root_/
//                                              ^^^^^^^^^^^^^ definition local57
//                                                             ^^^^^^^^^^ reference _root_/
//                                                                           ^^^^^^^^^^^^^^^^^^ definition local59
    int modelIndex = getModelPosition(modelToInsertAfter);
//      ^^^^^^^^^^ definition local61
//                   ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#getModelPosition#
//                                    ^^^^^^^^^^^^^^^^^^ reference local63
    if (modelIndex == -1) {
//      ^^^^^^^^^^ reference local64
      throw new IllegalStateException("Model is not added: " + modelToInsertAfter);
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
//                                                             ^^^^^^^^^^^^^^^^^^ reference local65
    }

    int targetIndex = modelIndex + 1;
//      ^^^^^^^^^^^ definition local66
//                    ^^^^^^^^^^ reference local68
    pauseModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
    models.add(targetIndex, modelToInsert);
//  ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//         ^^^ reference java/util/List#add(+1).
//             ^^^^^^^^^^^ reference local69
//                          ^^^^^^^^^^^^^ reference local70
    resumeModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().

    notifyItemInserted(targetIndex);
//  ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemInserted#
//                     ^^^^^^^^^^^ reference local71
  }

  /**
   * If the given model exists it is removed and an item removal is notified. Otherwise this does
   * nothing.
   */
  protected void removeModel(EpoxyModel<?> model) {
//               ^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#removeModel().
//                           ^^^^^^^^^^ reference _root_/
//                                         ^^^^^ definition local72
    int index = getModelPosition(model);
//      ^^^^^ definition local74
//              ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#getModelPosition#
//                               ^^^^^ reference local76
    if (index != -1) {
//      ^^^^^ reference local77
      pauseModelListNotifications();
//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
      models.remove(index);
//    ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//           ^^^^^^ reference java/util/List#remove(+1).
//                  ^^^^^ reference local78
      resumeModelListNotifications();
//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().

      notifyItemRemoved(index);
//    ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemRemoved#
//                      ^^^^^ reference local79
    }
  }

  /**
   * Removes all models
   */
  protected void removeAllModels() {
//               ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#removeAllModels().
    int numModelsRemoved = models.size();
//      ^^^^^^^^^^^^^^^^ definition local80
//                         ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                                ^^^^ reference java/util/List#size().

    pauseModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
    models.clear();
//  ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//         ^^^^^ reference java/util/List#clear().
    resumeModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().

    notifyItemRangeRemoved(0, numModelsRemoved);
//  ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemRangeRemoved#
//                            ^^^^^^^^^^^^^^^^ reference local82
  }

  /**
   * Removes all models after the given model, which must have already been added. An example use
   * case is you want to keep a header but clear everything else, like in the case of refreshing
   * data.
   */
  protected void removeAllAfterModel(EpoxyModel<?> model) {
//               ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#removeAllAfterModel().
//                                   ^^^^^^^^^^ reference _root_/
//                                                 ^^^^^ definition local83
    List<EpoxyModel<?>> modelsToRemove = getAllModelsAfter(model);
//  ^^^^ reference java/util/List#
//       ^^^^^^^^^^ reference _root_/
//                      ^^^^^^^^^^^^^^ definition local85
//                                       ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#getAllModelsAfter().
//                                                         ^^^^^ reference local87
    int numModelsRemoved = modelsToRemove.size();
//      ^^^^^^^^^^^^^^^^ definition local88
//                         ^^^^^^^^^^^^^^ reference local90
//                                        ^^^^ reference java/util/List#size().
    int initialModelCount = models.size();
//      ^^^^^^^^^^^^^^^^^ definition local91
//                          ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                                 ^^^^ reference java/util/List#size().

    // This is a sublist, so clearing it will clear the models in the original list
    pauseModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
    modelsToRemove.clear();
//  ^^^^^^^^^^^^^^ reference local93
//                 ^^^^^ reference java/util/List#clear().
    resumeModelListNotifications();
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().

    notifyItemRangeRemoved(initialModelCount - numModelsRemoved, numModelsRemoved);
//  ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyItemRangeRemoved#
//                         ^^^^^^^^^^^^^^^^^ reference local94
//                                             ^^^^^^^^^^^^^^^^ reference local95
//                                                               ^^^^^^^^^^^^^^^^ reference local96
  }

  /**
   * Sets the visibility of the given model, and notifies that the item changed if the new
   * visibility is different from the previous.
   *
   * @param model The model to show. It should already be added to the {@link #models} list.
   * @param show  True to show the model, false to hide it.
   */
  protected void showModel(EpoxyModel<?> model, boolean show) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#showModel().
//                         ^^^^^^^^^^ reference _root_/
//                                       ^^^^^ definition local97
//                                                      ^^^^ definition local99
    if (model.isShown() == show) {
//      ^^^^^ reference local101
//            ^^^^^^^ reference `<any>`#isShown#
//                         ^^^^ reference local102
      return;
    }

    model.show(show);
//  ^^^^^ reference local103
//        ^^^^ reference `<any>`#show#
//             ^^^^ reference local104
    notifyModelChanged(model);
//  ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#notifyModelChanged().
//                     ^^^^^ reference local105
  }

  /**
   * Shows the given model, and notifies that the item changed if the item wasn't already shown.
   *
   * @param model The model to show. It should already be added to the {@link #models} list.
   */
  protected void showModel(EpoxyModel<?> model) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#showModel(+1).
//                         ^^^^^^^^^^ reference _root_/
//                                       ^^^^^ definition local106
    showModel(model, true);
//  ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#showModel().
//            ^^^^^ reference local108
  }

  /**
   * Shows the given models, and notifies that each item changed if the item wasn't already shown.
   *
   * @param models The models to show. They should already be added to the {@link #models} list.
   */
  protected void showModels(EpoxyModel<?>... models) {
//               ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#showModels().
//                          ^^^^^^^^^^ reference _root_/
//                                           ^^^^^^ definition local109
    showModels(Arrays.asList(models));
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#showModels().
//             ^^^^^^ reference java/util/Arrays#
//                    ^^^^^^ reference java/util/Arrays#asList().
//                           ^^^^^^ reference local111
  }

  /**
   * Sets the visibility of the given models, and notifies that the items changed if the new
   * visibility is different from the previous.
   *
   * @param models The models to show. They should already be added to the {@link #models} list.
   * @param show   True to show the models, false to hide them.
   */
  protected void showModels(boolean show, EpoxyModel<?>... models) {
//               ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#showModels(+1).
//                                  ^^^^ definition local112
//                                        ^^^^^^^^^^ reference _root_/
//                                                         ^^^^^^ definition local114
    showModels(Arrays.asList(models), show);
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#showModels(+3).
//             ^^^^^^ reference java/util/Arrays#
//                    ^^^^^^ reference java/util/Arrays#asList().
//                           ^^^^^^ reference local116
//                                    ^^^^ reference local117
  }

  /**
   * Shows the given models, and notifies that each item changed if the item wasn't already shown.
   *
   * @param models The models to show. They should already be added to the {@link #models} list.
   */
  protected void showModels(Iterable<EpoxyModel<?>> models) {
//               ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#showModels(+2).
//                          ^^^^^^^^ reference java/lang/Iterable#
//                                   ^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^ definition local118
    showModels(models, true);
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#showModels(+3).
//             ^^^^^^ reference local120
  }

  /**
   * Sets the visibility of the given models, and notifies that the items changed if the new
   * visibility is different from the previous.
   *
   * @param models The models to show. They should already be added to the {@link #models} list.
   * @param show   True to show the models, false to hide them.
   */
  protected void showModels(Iterable<EpoxyModel<?>> models, boolean show) {
//               ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#showModels(+3).
//                          ^^^^^^^^ reference java/lang/Iterable#
//                                   ^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^ definition local121
//                                                                  ^^^^ definition local123
    for (EpoxyModel<?> model : models) {
//       ^^^^^^^^^^ reference _root_/
//                     ^^^^^ definition local125
//                             ^^^^^^ reference local127
      showModel(model, show);
//    ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#showModel().
//              ^^^^^ reference local128
//                     ^^^^ reference local129
    }
  }

  /**
   * Hides the given model, and notifies that the item changed if the item wasn't already hidden.
   *
   * @param model The model to hide. This should already be added to the {@link #models} list.
   */
  protected void hideModel(EpoxyModel<?> model) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#hideModel().
//                         ^^^^^^^^^^ reference _root_/
//                                       ^^^^^ definition local130
    showModel(model, false);
//  ^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#showModel().
//            ^^^^^ reference local132
  }

  /**
   * Hides the given models, and notifies that each item changed if the item wasn't already hidden.
   *
   * @param models The models to hide. They should already be added to the {@link #models} list.
   */
  protected void hideModels(Iterable<EpoxyModel<?>> models) {
//               ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#hideModels().
//                          ^^^^^^^^ reference java/lang/Iterable#
//                                   ^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^ definition local133
    showModels(models, false);
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#showModels(+3).
//             ^^^^^^ reference local135
  }

  /**
   * Hides the given models, and notifies that each item changed if the item wasn't already hidden.
   *
   * @param models The models to hide. They should already be added to the {@link #models} list.
   */
  protected void hideModels(EpoxyModel<?>... models) {
//               ^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#hideModels(+1).
//                          ^^^^^^^^^^ reference _root_/
//                                           ^^^^^^ definition local136
    hideModels(Arrays.asList(models));
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#hideModels().
//             ^^^^^^ reference java/util/Arrays#
//                    ^^^^^^ reference java/util/Arrays#asList().
//                           ^^^^^^ reference local138
  }

  /**
   * Hides all models currently located after the given model in the {@link #models} list.
   *
   * @param model The model after which to hide. It must exist in the {@link #models} list.
   */
  protected void hideAllAfterModel(EpoxyModel<?> model) {
//               ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#hideAllAfterModel().
//                                 ^^^^^^^^^^ reference _root_/
//                                               ^^^^^ definition local139
    hideModels(getAllModelsAfter(model));
//  ^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#hideModels().
//             ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#getAllModelsAfter().
//                               ^^^^^ reference local141
  }

  /**
   * Returns a sub list of all items in {@link #models} that occur after the given model. This list
   * is backed by the original models list, any changes to the returned list will be reflected in
   * the original {@link #models} list.
   *
   * @param model Must exist in {@link #models}.
   */
  protected List<EpoxyModel<?>> getAllModelsAfter(EpoxyModel<?> model) {
//          ^^^^ reference java/util/List#
//               ^^^^^^^^^^ reference _root_/
//                              ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#getAllModelsAfter().
//                                                ^^^^^^^^^^ reference _root_/
//                                                              ^^^^^ definition local142
    int index = getModelPosition(model);
//      ^^^^^ definition local144
//              ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#getModelPosition#
//                               ^^^^^ reference local146
    if (index == -1) {
//      ^^^^^ reference local147
      throw new IllegalStateException("Model is not added: " + model);
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
//                                                             ^^^^^ reference local148
    }
    return models.subList(index + 1, models.size());
//         ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                ^^^^^^^ reference java/util/List#subList().
//                        ^^^^^ reference local149
//                                   ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                                          ^^^^ reference java/util/List#size().
  }

  /**
   * We pause the list's notifications when we modify models internally, since we already do the
   * proper adapter notifications for those modifications. By pausing these list notifications we
   * prevent the differ having to do work to track them.
   */
  private void pauseModelListNotifications() {
//             ^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#pauseModelListNotifications().
    ((ModelList) models).pauseNotifications();
//    ^^^^^^^^^ reference _root_/
//               ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                       ^^^^^^^^^^^^^^^^^^ reference pauseNotifications#
  }

  private void resumeModelListNotifications() {
//             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/EpoxyAdapter#resumeModelListNotifications().
    ((ModelList) models).resumeNotifications();
//    ^^^^^^^^^ reference _root_/
//               ^^^^^^ reference com/airbnb/epoxy/EpoxyAdapter#models.
//                       ^^^^^^^^^^^^^^^^^^^ reference resumeNotifications#
  }
}

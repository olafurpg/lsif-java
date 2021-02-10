package com.airbnb.epoxy;

import java.util.HashMap;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^ reference java/util/HashMap#
import java.util.Map;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^ reference java/util/Map#

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#
import androidx.annotation.VisibleForTesting;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^^^^^^^^^^ reference androidx/annotation/VisibleForTesting#

class ViewTypeManager {
^^^^^^ definition com/airbnb/epoxy/ViewTypeManager#`<init>`().
^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewTypeManager#
  private static final Map<Class, Integer> VIEW_TYPE_MAP = new HashMap<>();
//                     ^^^ reference java/util/Map#
//                         ^^^^^ reference java/lang/Class#
//                                ^^^^^^^ reference java/lang/Integer#
//                                         ^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewTypeManager#VIEW_TYPE_MAP.
//                                                         ^^^^^^^^^^^^^^^ reference java/util/HashMap#`<init>`(+2).
//                                                             ^^^^^^^ reference java/util/HashMap#
  /**
   * The last model that had its view type looked up. This is stored so in most cases we can quickly
   * look up what view type belongs to which model.
   */
  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  EpoxyModel<?> lastModelForViewTypeLookup;
  ^^^^^^^^^^ reference _root_/
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewTypeManager#lastModelForViewTypeLookup.

  /**
   * The type map is static so that models of the same class share the same views across different
   * adapters. This is useful for view recycling when the adapter instance changes, or when there
   * are multiple adapters. For testing purposes though it is good to be able to clear the map so we
   * don't carry over values across tests.
   */
  @VisibleForTesting
   ^^^^^^^^^^^^^^^^^ reference androidx/annotation/VisibleForTesting#
  void resetMapForTesting() {
//     ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewTypeManager#resetMapForTesting().
    VIEW_TYPE_MAP.clear();
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#VIEW_TYPE_MAP.
//                ^^^^^ reference java/util/Map#clear().
  }

  int getViewTypeAndRememberModel(EpoxyModel<?> model) {
//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewTypeManager#getViewTypeAndRememberModel().
//                                ^^^^^^^^^^ reference _root_/
//                                              ^^^^^ definition local0
    lastModelForViewTypeLookup = model;
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#lastModelForViewTypeLookup.
//                               ^^^^^ reference local2
    return getViewType(model);
//         ^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#getViewType().
//                     ^^^^^ reference local3
  }

  static int getViewType(EpoxyModel<?> model) {
//           ^^^^^^^^^^^ definition com/airbnb/epoxy/ViewTypeManager#getViewType().
//                       ^^^^^^^^^^ reference _root_/
//                                     ^^^^^ definition local4
    int defaultViewType = model.getViewType();
//      ^^^^^^^^^^^^^^^ definition local6
//                        ^^^^^ reference local8
//                              ^^^^^^^^^^^ reference `<any>`#getViewType#
    if (defaultViewType != 0) {
//      ^^^^^^^^^^^^^^^ reference local9
      return defaultViewType;
//           ^^^^^^^^^^^^^^^ reference local10
    }

    // If a model does not specify a view type then we generate a value to use for models of that
    // class.
    Class modelClass = model.getClass();
//  ^^^^^ reference java/lang/Class#
//        ^^^^^^^^^^ definition local11
//                     ^^^^^ reference local13
//                           ^^^^^^^^ reference `<any>`#getClass#

    Integer viewType = VIEW_TYPE_MAP.get(modelClass);
//  ^^^^^^^ reference java/lang/Integer#
//          ^^^^^^^^ definition local14
//                     ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#VIEW_TYPE_MAP.
//                                   ^^^ reference java/util/Map#get().
//                                       ^^^^^^^^^^ reference local16

    if (viewType == null) {
//      ^^^^^^^^ reference local17
      viewType = -VIEW_TYPE_MAP.size() - 1;
//    ^^^^^^^^ reference local18
//                ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#VIEW_TYPE_MAP.
//                              ^^^^ reference java/util/Map#size().
      VIEW_TYPE_MAP.put(modelClass, viewType);
//    ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#VIEW_TYPE_MAP.
//                  ^^^ reference java/util/Map#put().
//                      ^^^^^^^^^^ reference local19
//                                  ^^^^^^^^ reference local20
    }

    return viewType;
//         ^^^^^^^^ reference local21
  }

  /**
   * Find the model that has the given view type so we can create a view for that model. In most
   * cases this value is a layout resource and we could simply inflate it, but to support {@link
   * EpoxyModelWithView} we can't assume the view type is a layout. In that case we need to lookup
   * the model so we can ask it to create a new view for itself.
   * <p>
   * To make this efficient, we rely on the RecyclerView implementation detail that {@link
   * BaseEpoxyAdapter#getItemViewType(int)} is called immediately before {@link
   * BaseEpoxyAdapter#onCreateViewHolder(android.view.ViewGroup, int)} . We cache the last model
   * that had its view type looked up, and unless that implementation changes we expect to have a
   * very fast lookup for the correct model.
   * <p>
   * To be safe, we fallback to searching through all models for a view type match. This is slow and
   * shouldn't be needed, but is a guard against recyclerview behavior changing.
   */
  EpoxyModel<?> getModelForViewType(BaseEpoxyAdapter adapter, int viewType) {
  ^^^^^^^^^^ reference _root_/
//              ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewTypeManager#getModelForViewType().
//                                  ^^^^^^^^^^^^^^^^ reference _root_/
//                                                   ^^^^^^^ definition local22
//                                                                ^^^^^^^^ definition local24
    if (lastModelForViewTypeLookup != null
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#lastModelForViewTypeLookup.
        && getViewType(lastModelForViewTypeLookup) == viewType) {
//         ^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#getViewType().
//                     ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#lastModelForViewTypeLookup.
//                                                    ^^^^^^^^ reference local26
      // We expect this to be a hit 100% of the time
      return lastModelForViewTypeLookup;
//           ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#lastModelForViewTypeLookup.
    }

    adapter.onExceptionSwallowed(
//  ^^^^^^^ reference local27
//          ^^^^^^^^^^^^^^^^^^^^ reference onExceptionSwallowed#
        new IllegalStateException("Last model did not match expected view type"));
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//          ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#

    // To be extra safe in case RecyclerView implementation details change...
    for (EpoxyModel<?> model : adapter.getCurrentModels()) {
//       ^^^^^^^^^^ reference _root_/
//                     ^^^^^ definition local28
//                             ^^^^^^^ reference local30
//                                     ^^^^^^^^^^^^^^^^ reference getCurrentModels#
      if (getViewType(model) == viewType) {
//        ^^^^^^^^^^^ reference com/airbnb/epoxy/ViewTypeManager#getViewType().
//                    ^^^^^ reference local31
//                              ^^^^^^^^ reference local32
        return model;
//             ^^^^^ reference local33
      }
    }

    // Check for the hidden model.
    HiddenEpoxyModel hiddenEpoxyModel = new HiddenEpoxyModel();
//  ^^^^^^^^^^^^^^^^ reference _root_/
//                   ^^^^^^^^^^^^^^^^ definition local34
//                                          ^^^^^^^^^^^^^^^^ reference _root_/
    if (viewType == hiddenEpoxyModel.getViewType()) {
//      ^^^^^^^^ reference local36
//                  ^^^^^^^^^^^^^^^^ reference local37
//                                   ^^^^^^^^^^^ reference getViewType#
      return hiddenEpoxyModel;
//           ^^^^^^^^^^^^^^^^ reference local38
    }

    throw new IllegalStateException("Could not find model for view type: " + viewType);
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//            ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
//                                                                           ^^^^^^^^ reference local39
  }
}

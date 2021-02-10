package com.airbnb.epoxy;

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
import androidx.annotation.VisibleForTesting;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^^^^^^^^^^ reference androidx/annotation/VisibleForTesting#
import androidx.collection.LongSparseArray;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/collection/
//                         ^^^^^^^^^^^^^^^ reference androidx/collection/LongSparseArray#

/**
 * A helper class for tracking changed models found by the {@link com.airbnb.epoxy.DiffHelper} to
 * be included as a payload in the
 * {@link androidx.recyclerview.widget.RecyclerView.Adapter#notifyItemChanged(int, Object)}
 * call.
 */
public class DiffPayload {
//     ^^^^^^^^^^^ definition com/airbnb/epoxy/DiffPayload#
  private final EpoxyModel<?> singleModel;
//              ^^^^^^^^^^ reference _root_/
//                            ^^^^^^^^^^^ definition com/airbnb/epoxy/DiffPayload#singleModel.
  private final LongSparseArray<EpoxyModel<?>> modelsById;
//              ^^^^^^^^^^^^^^^ reference androidx/collection/LongSparseArray#
//                              ^^^^^^^^^^ reference _root_/
//                                             ^^^^^^^^^^ definition com/airbnb/epoxy/DiffPayload#modelsById.

  DiffPayload(List<? extends EpoxyModel<?>> models) {
  ^^^^^^ definition com/airbnb/epoxy/DiffPayload#`<init>`().
//            ^^^^ reference java/util/List#
//                           ^^^^^^^^^^ reference _root_/
//                                          ^^^^^^ definition local0
    if (models.isEmpty()) {
//      ^^^^^^ reference local2
//             ^^^^^^^ reference java/util/List#isEmpty().
      throw new IllegalStateException("Models must not be empty");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
    }

    int modelCount = models.size();
//      ^^^^^^^^^^ definition local3
//                   ^^^^^^ reference local5
//                          ^^^^ reference java/util/List#size().

    if (modelCount == 1) {
//      ^^^^^^^^^^ reference local6
      // Optimize for the common case of only one model changed.
      singleModel = models.get(0);
//    ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#singleModel.
//                  ^^^^^^ reference local7
//                         ^^^ reference java/util/List#get().
      modelsById = null;
//    ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
    } else {
      singleModel = null;
//    ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#singleModel.
      modelsById = new LongSparseArray<>(modelCount);
//    ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                 ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference androidx/collection/LongSparseArray#`<init>`(+1).
//                     ^^^^^^^^^^^^^^^ reference androidx/collection/LongSparseArray#
//                                       ^^^^^^^^^^ reference local8
      for (EpoxyModel<?> model : models) {
//         ^^^^^^^^^^ reference _root_/
//                       ^^^^^ definition local9
//                               ^^^^^^ reference local11
        modelsById.put(model.id(), model);
//      ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                 ^^^ reference androidx/collection/LongSparseArray#put().
//                     ^^^^^ reference local12
//                           ^^ reference `<any>`#id#
//                                 ^^^^^ reference local13
      }
    }
  }

  public DiffPayload(EpoxyModel<?> changedItem) {
//       ^^^^^^ definition com/airbnb/epoxy/DiffPayload#`<init>`(+1).
//                   ^^^^^^^^^^ reference _root_/
//                                 ^^^^^^^^^^^ definition local14
    this(Collections.singletonList(changedItem));
//  ^^^^ reference com/airbnb/epoxy/DiffPayload#`<init>`().
//       ^^^^^^^^^^^ reference java/util/Collections#
//                   ^^^^^^^^^^^^^ reference java/util/Collections#singletonList().
//                                 ^^^^^^^^^^^ reference local16
  }

  /**
   * Looks through the payloads list and returns the first model found with the given model id. This
   * assumes that the payloads list will only contain objects of type {@link DiffPayload}, and will
   * throw if an unexpected type is found.
   */
  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  public static EpoxyModel<?> getModelFromPayload(List<Object> payloads, long modelId) {
//              ^^^^^^^^^^ reference _root_/
//                            ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffPayload#getModelFromPayload().
//                                                ^^^^ reference java/util/List#
//                                                     ^^^^^^ reference java/lang/Object#
//                                                             ^^^^^^^^ definition local17
//                                                                            ^^^^^^^ definition local19
    if (payloads.isEmpty()) {
//      ^^^^^^^^ reference local21
//               ^^^^^^^ reference java/util/List#isEmpty().
      return null;
    }

    for (Object payload : payloads) {
//       ^^^^^^ reference java/lang/Object#
//              ^^^^^^^ definition local22
//                        ^^^^^^^^ reference local24
      DiffPayload diffPayload = (DiffPayload) payload;
//    ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#
//                ^^^^^^^^^^^ definition local25
//                               ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#
//                                            ^^^^^^^ reference local27

      if (diffPayload.singleModel != null) {
//        ^^^^^^^^^^^ reference local28
//                    ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#singleModel.
        if (diffPayload.singleModel.id() == modelId) {
//          ^^^^^^^^^^^ reference local29
//                      ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#singleModel.
//                                  ^^ reference `<any>`#id#
//                                          ^^^^^^^ reference local30
          return diffPayload.singleModel;
//               ^^^^^^^^^^^ reference local31
//                           ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#singleModel.
        }
      } else {
        EpoxyModel<?> modelForId = diffPayload.modelsById.get(modelId);
//      ^^^^^^^^^^ reference _root_/
//                    ^^^^^^^^^^ definition local32
//                                 ^^^^^^^^^^^ reference local34
//                                             ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                                                        ^^^ reference androidx/collection/LongSparseArray#get().
//                                                            ^^^^^^^ reference local35
        if (modelForId != null) {
//          ^^^^^^^^^^ reference local36
          return modelForId;
//               ^^^^^^^^^^ reference local37
        }
      }
    }

    return null;
  }

  @VisibleForTesting
   ^^^^^^^^^^^^^^^^^ reference androidx/annotation/VisibleForTesting#
  boolean equalsForTesting(DiffPayload that) {
//        ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffPayload#equalsForTesting().
//                         ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#
//                                     ^^^^ definition local38
    if (singleModel != null) {
//      ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#singleModel.
      return that.singleModel == singleModel;
//           ^^^^ reference local40
//                ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#singleModel.
//                               ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#singleModel.
    }

    int thisSize = modelsById.size();
//      ^^^^^^^^ definition local41
//                 ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                            ^^^^ reference androidx/collection/LongSparseArray#size().
    int thatSize = that.modelsById.size();
//      ^^^^^^^^ definition local43
//                 ^^^^ reference local45
//                      ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                                 ^^^^ reference androidx/collection/LongSparseArray#size().

    if (thisSize != thatSize) {
//      ^^^^^^^^ reference local46
//                  ^^^^^^^^ reference local47
      return false;
    }

    for (int i = 0; i < thisSize; i++) {
//           ^ definition local48
//                  ^ reference local50
//                      ^^^^^^^^ reference local51
//                                ^ reference local52
      long thisKey = modelsById.keyAt(i);
//         ^^^^^^^ definition local53
//                   ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                              ^^^^^ reference androidx/collection/LongSparseArray#keyAt().
//                                    ^ reference local55
      long thatKey = that.modelsById.keyAt(i);
//         ^^^^^^^ definition local56
//                   ^^^^ reference local58
//                        ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                                   ^^^^^ reference androidx/collection/LongSparseArray#keyAt().
//                                         ^ reference local59

      if (thisKey != thatKey) {
//        ^^^^^^^ reference local60
//                   ^^^^^^^ reference local61
        return false;
      }

      EpoxyModel<?> thisModel = modelsById.valueAt(i);
//    ^^^^^^^^^^ reference _root_/
//                  ^^^^^^^^^ definition local62
//                              ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                                         ^^^^^^^ reference androidx/collection/LongSparseArray#valueAt().
//                                                 ^ reference local64
      EpoxyModel<?> thatModel = that.modelsById.valueAt(i);
//    ^^^^^^^^^^ reference _root_/
//                  ^^^^^^^^^ definition local65
//                              ^^^^ reference local67
//                                   ^^^^^^^^^^ reference com/airbnb/epoxy/DiffPayload#modelsById.
//                                              ^^^^^^^ reference androidx/collection/LongSparseArray#valueAt().
//                                                      ^ reference local68
      if (thisModel != thatModel) {
//        ^^^^^^^^^ reference local69
//                     ^^^^^^^^^ reference local70
        return false;
      }
    }

    return true;
  }
}

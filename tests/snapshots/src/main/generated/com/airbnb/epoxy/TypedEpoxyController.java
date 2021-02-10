package com.airbnb.epoxy;

import android.os.Handler;
//     ^^^^^^^ reference android/
//             ^^ reference android/os/
//                ^^^^^^^ reference android/os/Handler#
import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#

/**
 * This is a wrapper around {@link com.airbnb.epoxy.EpoxyController} to simplify how data is
 * accessed. Use this if the data required to build your models is represented by a single object.
 * <p>
 * To use this, create a subclass typed with your data object. Then, call {@link #setData(Object)}
 * whenever that data changes. This class will handle calling {@link #buildModels(Object)} with the
 * latest data.
 * <p>
 * You should NOT call {@link #requestModelBuild()} directly.
 *
 * @see Typed2EpoxyController
 * @see Typed3EpoxyController
 * @see Typed4EpoxyController
 */
public abstract class TypedEpoxyController<T> extends EpoxyController {
//              ^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#
//                                                    ^^^^^^^^^^^^^^^ reference _root_/
  private T currentData;
//        ^ reference com/airbnb/epoxy/TypedEpoxyController#[T]
//          ^^^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#currentData.
  private boolean allowModelBuildRequests;
//                ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#allowModelBuildRequests.

  public TypedEpoxyController() {
//       ^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#`<init>`().
  }

  public TypedEpoxyController(Handler modelBuildingHandler, Handler diffingHandler) {
//       ^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#`<init>`(+1).
//                            ^^^^^^^ reference _root_/
//                                    ^^^^^^^^^^^^^^^^^^^^ definition local0
//                                                          ^^^^^^^ reference _root_/
//                                                                  ^^^^^^^^^^^^^^ definition local2
    super(modelBuildingHandler, diffingHandler);
//        ^^^^^^^^^^^^^^^^^^^^ reference local4
//                              ^^^^^^^^^^^^^^ reference local5
  }

  public final void setData(T data) {
//                  ^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#setData().
//                          ^ reference com/airbnb/epoxy/TypedEpoxyController#[T]
//                            ^^^^ definition local6
    currentData = data;
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#currentData.
//                ^^^^ reference local8
    allowModelBuildRequests = true;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#allowModelBuildRequests.
    requestModelBuild();
//  ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#requestModelBuild().
    allowModelBuildRequests = false;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#allowModelBuildRequests.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void requestModelBuild() {
//                  ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#requestModelBuild().
    if (!allowModelBuildRequests) {
//       ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#allowModelBuildRequests.
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 2:47
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "You cannot call `requestModelBuild` directly. Call `setData` instead to trigger a "
              + "model refresh with new data.");
    }
    super.requestModelBuild();
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^ reference requestModelBuild#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void moveModel(int fromPosition, int toPosition) {
//            ^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#moveModel().
//                          ^^^^^^^^^^^^ definition local9
//                                            ^^^^^^^^^^ definition local11
    allowModelBuildRequests = true;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#allowModelBuildRequests.
    super.moveModel(fromPosition, toPosition);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference moveModel#
//                  ^^^^^^^^^^^^ reference local13
//                                ^^^^^^^^^^ reference local14
    allowModelBuildRequests = false;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#allowModelBuildRequests.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void requestDelayedModelBuild(int delayMs) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#requestDelayedModelBuild().
//                                         ^^^^^^^ definition local15
    if (!allowModelBuildRequests) {
//       ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#allowModelBuildRequests.
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 2:47
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "You cannot call `requestModelBuild` directly. Call `setData` instead to trigger a "
              + "model refresh with new data.");
    }
    super.requestDelayedModelBuild(delayMs);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^^^ reference requestDelayedModelBuild#
//                                 ^^^^^^^ reference local17
  }

  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  public final T getCurrentData() {
//             ^ reference com/airbnb/epoxy/TypedEpoxyController#[T]
//               ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#getCurrentData().
    return currentData;
//         ^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#currentData.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected final void buildModels() {
//                     ^^^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#buildModels().
    if (!isBuildingModels()) {
//       ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#isBuildingModels#
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 2:41
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "You cannot call `buildModels` directly. Call `setData` instead to trigger a model "
              + "refresh with new data.");
    }
    buildModels(currentData);
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#buildModels(+1).
//              ^^^^^^^^^^^ reference com/airbnb/epoxy/TypedEpoxyController#currentData.
  }

  protected abstract void buildModels(T data);
//                        ^^^^^^^^^^^ definition com/airbnb/epoxy/TypedEpoxyController#buildModels(+1).
//                                    ^ reference com/airbnb/epoxy/TypedEpoxyController#[T]
//                                      ^^^^ definition local18
}

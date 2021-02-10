package com.airbnb.epoxy;

import android.os.Handler;
//     ^^^^^^^ reference android/
//             ^^ reference android/os/
//                ^^^^^^^ reference android/os/Handler#

/**
 * This is a wrapper around {@link com.airbnb.epoxy.EpoxyController} to simplify how data is
 * accessed. Use this if the data required to build your models is represented by three objects.
 * <p>
 * To use this, create a subclass typed with your data object. Then, call {@link #setData}
 * whenever that data changes. This class will handle calling {@link #buildModels} with the
 * latest data.
 * <p>
 * You should NOT call {@link #requestModelBuild()} directly.
 *
 * @see TypedEpoxyController
 * @see Typed2EpoxyController
 * @see Typed4EpoxyController
 */
public abstract class Typed3EpoxyController<T, U, V> extends EpoxyController {
//              ^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#
//                                                           ^^^^^^^^^^^^^^^ reference _root_/

  private T data1;
//        ^ reference com/airbnb/epoxy/Typed3EpoxyController#[T]
//          ^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#data1.
  private U data2;
//        ^ reference com/airbnb/epoxy/Typed3EpoxyController#[U]
//          ^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#data2.
  private V data3;
//        ^ reference com/airbnb/epoxy/Typed3EpoxyController#[V]
//          ^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#data3.
  private boolean allowModelBuildRequests;
//                ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#allowModelBuildRequests.

  public Typed3EpoxyController() {
//       ^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#`<init>`().
  }

  public Typed3EpoxyController(Handler modelBuildingHandler, Handler diffingHandler) {
//       ^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#`<init>`(+1).
//                             ^^^^^^^ reference _root_/
//                                     ^^^^^^^^^^^^^^^^^^^^ definition local0
//                                                           ^^^^^^^ reference _root_/
//                                                                   ^^^^^^^^^^^^^^ definition local2
    super(modelBuildingHandler, diffingHandler);
//        ^^^^^^^^^^^^^^^^^^^^ reference local4
//                              ^^^^^^^^^^^^^^ reference local5
  }

  /**
   * Call this with the latest data when you want models to be rebuilt. The data will be passed on
   * to {@link #buildModels(Object, Object, Object)}
   */
  public void setData(T data1, U data2, V data3) {
//            ^^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#setData().
//                    ^ reference com/airbnb/epoxy/Typed3EpoxyController#[T]
//                      ^^^^^ definition local6
//                             ^ reference com/airbnb/epoxy/Typed3EpoxyController#[U]
//                               ^^^^^ definition local8
//                                      ^ reference com/airbnb/epoxy/Typed3EpoxyController#[V]
//                                        ^^^^^ definition local10
    this.data1 = data1;
//  ^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#this.
//       ^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#data1.
//               ^^^^^ reference local12
    this.data2 = data2;
//  ^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#this.
//       ^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#data2.
//               ^^^^^ reference local13
    this.data3 = data3;
//  ^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#this.
//       ^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#data3.
//               ^^^^^ reference local14
    allowModelBuildRequests = true;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#allowModelBuildRequests.
    requestModelBuild();
//  ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#requestModelBuild().
    allowModelBuildRequests = false;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#allowModelBuildRequests.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void requestModelBuild() {
//                  ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#requestModelBuild().
    if (!allowModelBuildRequests) {
//       ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#allowModelBuildRequests.
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
//            ^^^^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#moveModel().
//                          ^^^^^^^^^^^^ definition local15
//                                            ^^^^^^^^^^ definition local17
    allowModelBuildRequests = true;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#allowModelBuildRequests.
    super.moveModel(fromPosition, toPosition);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference moveModel#
//                  ^^^^^^^^^^^^ reference local19
//                                ^^^^^^^^^^ reference local20
    allowModelBuildRequests = false;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#allowModelBuildRequests.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void requestDelayedModelBuild(int delayMs) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#requestDelayedModelBuild().
//                                         ^^^^^^^ definition local21
    if (!allowModelBuildRequests) {
//       ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#allowModelBuildRequests.
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 2:47
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "You cannot call `requestModelBuild` directly. Call `setData` instead to trigger a "
              + "model refresh with new data.");
    }
    super.requestDelayedModelBuild(delayMs);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^^^ reference requestDelayedModelBuild#
//                                 ^^^^^^^ reference local23
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected final void buildModels() {
//                     ^^^^^^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#buildModels().
    if (!isBuildingModels()) {
//       ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#isBuildingModels#
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 2:41
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "You cannot call `buildModels` directly. Call `setData` instead to trigger a model "
              + "refresh with new data.");
    }
    buildModels(data1, data2, data3);
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#buildModels(+1).
//              ^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#data1.
//                     ^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#data2.
//                            ^^^^^ reference com/airbnb/epoxy/Typed3EpoxyController#data3.
  }

  protected abstract void buildModels(T data1, U data2, V data3);
//                        ^^^^^^^^^^^ definition com/airbnb/epoxy/Typed3EpoxyController#buildModels(+1).
//                                    ^ reference com/airbnb/epoxy/Typed3EpoxyController#[T]
//                                      ^^^^^ definition local24
//                                             ^ reference com/airbnb/epoxy/Typed3EpoxyController#[U]
//                                               ^^^^^ definition local26
//                                                      ^ reference com/airbnb/epoxy/Typed3EpoxyController#[V]
//                                                        ^^^^^ definition local28
}

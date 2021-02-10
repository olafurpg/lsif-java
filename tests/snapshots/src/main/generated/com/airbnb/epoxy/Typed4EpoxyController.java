package com.airbnb.epoxy;

import android.os.Handler;
//     ^^^^^^^ reference android/
//             ^^ reference android/os/
//                ^^^^^^^ reference android/os/Handler#

/**
 * This is a wrapper around {@link com.airbnb.epoxy.EpoxyController} to simplify how data is
 * accessed. Use this if the data required to build your models is represented by four objects.
 * <p>
 * To use this, create a subclass typed with your data object. Then, call {@link #setData}
 * whenever that data changes. This class will handle calling {@link #buildModels} with the
 * latest data.
 * <p>
 * You should NOT call {@link #requestModelBuild()} directly.
 *
 * @see TypedEpoxyController
 * @see Typed2EpoxyController
 * @see Typed3EpoxyController
 */
public abstract class Typed4EpoxyController<T, U, V, W> extends EpoxyController {
//              ^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#
//                                                              ^^^^^^^^^^^^^^^ reference _root_/

  private T data1;
//        ^ reference com/airbnb/epoxy/Typed4EpoxyController#[T]
//          ^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#data1.
  private U data2;
//        ^ reference com/airbnb/epoxy/Typed4EpoxyController#[U]
//          ^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#data2.
  private V data3;
//        ^ reference com/airbnb/epoxy/Typed4EpoxyController#[V]
//          ^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#data3.
  private W data4;
//        ^ reference com/airbnb/epoxy/Typed4EpoxyController#[W]
//          ^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#data4.
  private boolean allowModelBuildRequests;
//                ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#allowModelBuildRequests.

  public Typed4EpoxyController() {
//       ^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#`<init>`().
  }

  public Typed4EpoxyController(Handler modelBuildingHandler, Handler diffingHandler) {
//       ^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#`<init>`(+1).
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
   * to {@link #buildModels(Object, Object, Object, Object)}
   */
  public void setData(T data1, U data2, V data3, W data4) {
//            ^^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#setData().
//                    ^ reference com/airbnb/epoxy/Typed4EpoxyController#[T]
//                      ^^^^^ definition local6
//                             ^ reference com/airbnb/epoxy/Typed4EpoxyController#[U]
//                               ^^^^^ definition local8
//                                      ^ reference com/airbnb/epoxy/Typed4EpoxyController#[V]
//                                        ^^^^^ definition local10
//                                               ^ reference com/airbnb/epoxy/Typed4EpoxyController#[W]
//                                                 ^^^^^ definition local12
    this.data1 = data1;
//  ^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#this.
//       ^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#data1.
//               ^^^^^ reference local14
    this.data2 = data2;
//  ^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#this.
//       ^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#data2.
//               ^^^^^ reference local15
    this.data3 = data3;
//  ^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#this.
//       ^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#data3.
//               ^^^^^ reference local16
    this.data4 = data4;
//  ^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#this.
//       ^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#data4.
//               ^^^^^ reference local17
    allowModelBuildRequests = true;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#allowModelBuildRequests.
    requestModelBuild();
//  ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#requestModelBuild().
    allowModelBuildRequests = false;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#allowModelBuildRequests.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public final void requestModelBuild() {
//                  ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#requestModelBuild().
    if (!allowModelBuildRequests) {
//       ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#allowModelBuildRequests.
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
//            ^^^^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#moveModel().
//                          ^^^^^^^^^^^^ definition local18
//                                            ^^^^^^^^^^ definition local20
    allowModelBuildRequests = true;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#allowModelBuildRequests.
    super.moveModel(fromPosition, toPosition);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference moveModel#
//                  ^^^^^^^^^^^^ reference local22
//                                ^^^^^^^^^^ reference local23
    allowModelBuildRequests = false;
//  ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#allowModelBuildRequests.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void requestDelayedModelBuild(int delayMs) {
//            ^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#requestDelayedModelBuild().
//                                         ^^^^^^^ definition local24
    if (!allowModelBuildRequests) {
//       ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#allowModelBuildRequests.
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 2:47
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "You cannot call `requestModelBuild` directly. Call `setData` instead to trigger a "
              + "model refresh with new data.");
    }
    super.requestDelayedModelBuild(delayMs);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^^^^^^ reference requestDelayedModelBuild#
//                                 ^^^^^^^ reference local26
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected final void buildModels() {
//                     ^^^^^^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#buildModels().
    if (!isBuildingModels()) {
//       ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#isBuildingModels#
      throw new IllegalStateException(
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 2:41
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          "You cannot call `buildModels` directly. Call `setData` instead to trigger a model "
              + "refresh with new data.");
    }
    buildModels(data1, data2, data3, data4);
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#buildModels(+1).
//              ^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#data1.
//                     ^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#data2.
//                            ^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#data3.
//                                   ^^^^^ reference com/airbnb/epoxy/Typed4EpoxyController#data4.
  }

  protected abstract void buildModels(T data1, U data2, V data3, W data4);
//                        ^^^^^^^^^^^ definition com/airbnb/epoxy/Typed4EpoxyController#buildModels(+1).
//                                    ^ reference com/airbnb/epoxy/Typed4EpoxyController#[T]
//                                      ^^^^^ definition local27
//                                             ^ reference com/airbnb/epoxy/Typed4EpoxyController#[U]
//                                               ^^^^^ definition local29
//                                                      ^ reference com/airbnb/epoxy/Typed4EpoxyController#[V]
//                                                        ^^^^^ definition local31
//                                                               ^ reference com/airbnb/epoxy/Typed4EpoxyController#[W]
//                                                                 ^^^^^ definition local33
}


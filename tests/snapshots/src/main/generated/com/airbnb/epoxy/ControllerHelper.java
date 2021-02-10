package com.airbnb.epoxy;

import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#

/**
 * A helper class for {@link EpoxyController} to handle {@link
 * com.airbnb.epoxy.AutoModel} models. This is only implemented by the generated classes created the
 * annotation processor.
 */
public abstract class ControllerHelper<T extends EpoxyController> {
//              ^^^^^^ definition com/airbnb/epoxy/ControllerHelper#`<init>`().
//              ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelper#
//                                               ^^^^^^^^^^^^^^^ reference _root_/
  public abstract void resetAutoModels();
//                     ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelper#resetAutoModels().

  protected void validateModelHashCodesHaveNotChanged(T controller) {
//               ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelper#validateModelHashCodesHaveNotChanged().
//                                                    ^ reference com/airbnb/epoxy/ControllerHelper#[T]
//                                                      ^^^^^^^^^^ definition local0
    List<EpoxyModel<?>> currentModels = controller.getAdapter().getCopyOfModels();
//  ^^^^ reference java/util/List#
//       ^^^^^^^^^^ reference _root_/
//                      ^^^^^^^^^^^^^ definition local2
//                                      ^^^^^^^^^^ reference local4
//                                                 ^^^^^^^^^^ reference getAdapter#
//                                                              ^^^^^^^^^^^^^^^ reference getAdapter#getCopyOfModels#

    for (int i = 0; i < currentModels.size(); i++) {
//           ^ definition local5
//                  ^ reference local7
//                      ^^^^^^^^^^^^^ reference local8
//                                    ^^^^ reference java/util/List#size().
//                                            ^ reference local9
      EpoxyModel model = currentModels.get(i);
//    ^^^^^^^^^^ reference _root_/
//               ^^^^^ definition local10
//                       ^^^^^^^^^^^^^ reference local12
//                                     ^^^ reference java/util/List#get().
//                                         ^ reference local13
      model.validateStateHasNotChangedSinceAdded(
//    ^^^^^ reference local14
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference validateStateHasNotChangedSinceAdded#
          "Model has changed since it was added to the controller.", i);
//                                                                   ^ reference local15
    }
  }

  protected void setControllerToStageTo(EpoxyModel<?> model, T controller) {
//               ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelper#setControllerToStageTo().
//                                      ^^^^^^^^^^ reference _root_/
//                                                    ^^^^^ definition local16
//                                                           ^ reference com/airbnb/epoxy/ControllerHelper#[T]
//                                                             ^^^^^^^^^^ definition local18
    model.controllerToStageTo = controller;
//  ^^^^^ reference local20
//        ^^^^^^^^^^^^^^^^^^^ reference `<any>`#controllerToStageTo#
//                              ^^^^^^^^^^ reference local21
  }
}

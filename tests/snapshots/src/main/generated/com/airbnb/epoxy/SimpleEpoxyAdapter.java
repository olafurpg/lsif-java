package com.airbnb.epoxy;

import java.util.Collection;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^ reference java/util/Collection#
import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#

/**
 * A non-abstract version of {@link com.airbnb.epoxy.EpoxyAdapter} that exposes all methods and
 * models as public. Use this if you don't want to create your own adapter subclass and instead want
 * to modify the adapter from elsewhere, such as from an activity.
 */
public class SimpleEpoxyAdapter extends EpoxyAdapter {
//     ^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#`<init>`().
//     ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#
//                                      ^^^^^^^^^^^^ reference _root_/

  public List<EpoxyModel<?>> getModels() {
//       ^^^^ reference java/util/List#
//            ^^^^^^^^^^ reference _root_/
//                           ^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#getModels().
    return models;
//         ^^^^^^ reference _root_/
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void enableDiffing() {
//            ^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#enableDiffing().
    super.enableDiffing();
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^ reference enableDiffing#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void notifyModelsChanged() {
//            ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#notifyModelsChanged().
    super.notifyModelsChanged();
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^ reference notifyModelsChanged#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public BoundViewHolders getBoundViewHolders() {
//       ^^^^^^^^^^^^^^^^ reference _root_/
//                        ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#getBoundViewHolders().
    return super.getBoundViewHolders();
//         ^^^^^ reference _root_/
//               ^^^^^^^^^^^^^^^^^^^ reference getBoundViewHolders#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void notifyModelChanged(EpoxyModel<?> model) {
//            ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#notifyModelChanged().
//                               ^^^^^^^^^^ reference _root_/
//                                             ^^^^^ definition local0
    super.notifyModelChanged(model);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^ reference notifyModelChanged#
//                           ^^^^^ reference local2
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void addModels(EpoxyModel<?>... modelsToAdd) {
//            ^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#addModels().
//                      ^^^^^^^^^^ reference _root_/
//                                       ^^^^^^^^^^^ definition local3
    super.addModels(modelsToAdd);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference addModels#
//                  ^^^^^^^^^^^ reference local5
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void addModels(Collection<? extends EpoxyModel<?>> modelsToAdd) {
//            ^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#addModels(+1).
//                      ^^^^^^^^^^ reference java/util/Collection#
//                                           ^^^^^^^^^^ reference _root_/
//                                                          ^^^^^^^^^^^ definition local6
    super.addModels(modelsToAdd);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference addModels#
//                  ^^^^^^^^^^^ reference local8
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void insertModelBefore(EpoxyModel<?> modelToInsert, EpoxyModel<?> modelToInsertBefore) {
//            ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#insertModelBefore().
//                              ^^^^^^^^^^ reference _root_/
//                                            ^^^^^^^^^^^^^ definition local9
//                                                           ^^^^^^^^^^ reference _root_/
//                                                                         ^^^^^^^^^^^^^^^^^^^ definition local11
    super.insertModelBefore(modelToInsert, modelToInsertBefore);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^ reference insertModelBefore#
//                          ^^^^^^^^^^^^^ reference local13
//                                         ^^^^^^^^^^^^^^^^^^^ reference local14
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void insertModelAfter(EpoxyModel<?> modelToInsert, EpoxyModel<?> modelToInsertAfter) {
//            ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#insertModelAfter().
//                             ^^^^^^^^^^ reference _root_/
//                                           ^^^^^^^^^^^^^ definition local15
//                                                          ^^^^^^^^^^ reference _root_/
//                                                                        ^^^^^^^^^^^^^^^^^^ definition local17
    super.insertModelAfter(modelToInsert, modelToInsertAfter);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^ reference insertModelAfter#
//                         ^^^^^^^^^^^^^ reference local19
//                                        ^^^^^^^^^^^^^^^^^^ reference local20
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void removeModel(EpoxyModel<?> model) {
//            ^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#removeModel().
//                        ^^^^^^^^^^ reference _root_/
//                                      ^^^^^ definition local21
    super.removeModel(model);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^ reference removeModel#
//                    ^^^^^ reference local23
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void removeAllModels() {
//            ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#removeAllModels().
    super.removeAllModels();
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^ reference removeAllModels#
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void removeAllAfterModel(EpoxyModel<?> model) {
//            ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#removeAllAfterModel().
//                                ^^^^^^^^^^ reference _root_/
//                                              ^^^^^ definition local24
    super.removeAllAfterModel(model);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^^^ reference removeAllAfterModel#
//                            ^^^^^ reference local26
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void showModel(EpoxyModel<?> model, boolean show) {
//            ^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#showModel().
//                      ^^^^^^^^^^ reference _root_/
//                                    ^^^^^ definition local27
//                                                   ^^^^ definition local29
    super.showModel(model, show);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference showModel#
//                  ^^^^^ reference local31
//                         ^^^^ reference local32
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void showModel(EpoxyModel<?> model) {
//            ^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#showModel(+1).
//                      ^^^^^^^^^^ reference _root_/
//                                    ^^^^^ definition local33
    super.showModel(model);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference showModel#
//                  ^^^^^ reference local35
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void showModels(EpoxyModel<?>... models) {
//            ^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#showModels().
//                       ^^^^^^^^^^ reference _root_/
//                                        ^^^^^^ definition local36
    super.showModels(models);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^ reference showModels#
//                   ^^^^^^ reference local38
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void showModels(boolean show, EpoxyModel<?>... models) {
//            ^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#showModels(+1).
//                               ^^^^ definition local39
//                                     ^^^^^^^^^^ reference _root_/
//                                                      ^^^^^^ definition local41
    super.showModels(show, models);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^ reference showModels#
//                   ^^^^ reference local43
//                         ^^^^^^ reference local44
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void showModels(Iterable<EpoxyModel<?>> epoxyModels) {
//            ^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#showModels(+2).
//                       ^^^^^^^^ reference java/lang/Iterable#
//                                ^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^^ definition local45
    super.showModels(epoxyModels);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^ reference showModels#
//                   ^^^^^^^^^^^ reference local47
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void showModels(Iterable<EpoxyModel<?>> epoxyModels, boolean show) {
//            ^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#showModels(+3).
//                       ^^^^^^^^ reference java/lang/Iterable#
//                                ^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^^ definition local48
//                                                                    ^^^^ definition local50
    super.showModels(epoxyModels, show);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^ reference showModels#
//                   ^^^^^^^^^^^ reference local52
//                                ^^^^ reference local53
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void hideModel(EpoxyModel<?> model) {
//            ^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#hideModel().
//                      ^^^^^^^^^^ reference _root_/
//                                    ^^^^^ definition local54
    super.hideModel(model);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^ reference hideModel#
//                  ^^^^^ reference local56
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void hideModels(Iterable<EpoxyModel<?>> epoxyModels) {
//            ^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#hideModels().
//                       ^^^^^^^^ reference java/lang/Iterable#
//                                ^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^^ definition local57
    super.hideModels(epoxyModels);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^ reference hideModels#
//                   ^^^^^^^^^^^ reference local59
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void hideModels(EpoxyModel<?>... models) {
//            ^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#hideModels(+1).
//                       ^^^^^^^^^^ reference _root_/
//                                        ^^^^^^ definition local60
    super.hideModels(models);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^ reference hideModels#
//                   ^^^^^^ reference local62
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void hideAllAfterModel(EpoxyModel<?> model) {
//            ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#hideAllAfterModel().
//                              ^^^^^^^^^^ reference _root_/
//                                            ^^^^^ definition local63
    super.hideAllAfterModel(model);
//  ^^^^^ reference _root_/
//        ^^^^^^^^^^^^^^^^^ reference hideAllAfterModel#
//                          ^^^^^ reference local65
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public List<EpoxyModel<?>> getAllModelsAfter(EpoxyModel<?> model) {
//       ^^^^ reference java/util/List#
//            ^^^^^^^^^^ reference _root_/
//                           ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#getAllModelsAfter().
//                                             ^^^^^^^^^^ reference _root_/
//                                                           ^^^^^ definition local66
    return super.getAllModelsAfter(model);
//         ^^^^^ reference _root_/
//               ^^^^^^^^^^^^^^^^^ reference getAllModelsAfter#
//                                 ^^^^^ reference local68
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public int getModelPosition(EpoxyModel<?> model) {
//           ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/SimpleEpoxyAdapter#getModelPosition().
//                            ^^^^^^^^^^ reference _root_/
//                                          ^^^^^ definition local69
    return super.getModelPosition(model);
//         ^^^^^ reference _root_/
//               ^^^^^^^^^^^^^^^^ reference getModelPosition#
//                                ^^^^^ reference local71
  }
}

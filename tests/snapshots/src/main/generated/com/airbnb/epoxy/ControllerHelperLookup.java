package com.airbnb.epoxy;

import java.lang.reflect.Constructor;
//     ^^^^ reference java/
//          ^^^^ reference java/lang/
//               ^^^^^^^ reference java/lang/reflect/
//                       ^^^^^^^^^^^ reference java/lang/reflect/Constructor#
import java.lang.reflect.InvocationTargetException;
//     ^^^^ reference java/
//          ^^^^ reference java/lang/
//               ^^^^^^^ reference java/lang/reflect/
//                       ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/reflect/InvocationTargetException#
import java.util.LinkedHashMap;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^^^^ reference java/util/LinkedHashMap#
import java.util.Map;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^ reference java/util/Map#

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#

/**
 * Looks up a generated {@link ControllerHelper} implementation for a given adapter.
 * If the adapter has no {@link com.airbnb.epoxy.AutoModel} models then a No-Op implementation will
 * be returned.
 */
class ControllerHelperLookup {
^^^^^^ definition com/airbnb/epoxy/ControllerHelperLookup#`<init>`().
^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelperLookup#
  private static final String GENERATED_HELPER_CLASS_SUFFIX = "_EpoxyHelper";
//                     ^^^^^^ reference java/lang/String#
//                            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelperLookup#GENERATED_HELPER_CLASS_SUFFIX.
  private static final Map<Class<?>, Constructor<?>> BINDINGS = new LinkedHashMap<>();
//                     ^^^ reference java/util/Map#
//                         ^^^^^ reference java/lang/Class#
//                                   ^^^^^^^^^^^ reference java/lang/reflect/Constructor#
//                                                   ^^^^^^^^ definition com/airbnb/epoxy/ControllerHelperLookup#BINDINGS.
//                                                              ^^^^^^^^^^^^^^^^^^^^^ reference java/util/LinkedHashMap#`<init>`(+2).
//                                                                  ^^^^^^^^^^^^^ reference java/util/LinkedHashMap#
  private static final NoOpControllerHelper NO_OP_CONTROLLER_HELPER = new NoOpControllerHelper();
//                     ^^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                          ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelperLookup#NO_OP_CONTROLLER_HELPER.
//                                                                        ^^^^^^^^^^^^^^^^^^^^ reference _root_/

  static ControllerHelper getHelperForController(EpoxyController controller) {
//       ^^^^^^^^^^^^^^^^ reference _root_/
//                        ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelperLookup#getHelperForController().
//                                               ^^^^^^^^^^^^^^^ reference _root_/
//                                                               ^^^^^^^^^^ definition local0
    Constructor<?> constructor = findConstructorForClass(controller.getClass());
//  ^^^^^^^^^^^ reference java/lang/reflect/Constructor#
//                 ^^^^^^^^^^^ definition local2
//                               ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ControllerHelperLookup#findConstructorForClass#
//                                                       ^^^^^^^^^^ reference local4
//                                                                  ^^^^^^^^ reference getClass#
    if (constructor == null) {
//      ^^^^^^^^^^^ reference local5
      return NO_OP_CONTROLLER_HELPER;
//           ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ControllerHelperLookup#NO_OP_CONTROLLER_HELPER.
    }

    try {
      return (ControllerHelper) constructor.newInstance(controller);
//            ^^^^^^^^^^^^^^^^ reference _root_/
//                              ^^^^^^^^^^^ reference local6
//                                          ^^^^^^^^^^^ reference java/lang/reflect/Constructor#newInstance().
//                                                      ^^^^^^^^^^ reference local7
    } catch (IllegalAccessException e) {
//           ^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalAccessException#
//                                  ^ definition local8
      throw new RuntimeException("Unable to invoke " + constructor, e);
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#`<init>`(+2).
//              ^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#
//                                                     ^^^^^^^^^^^ reference local10
//                                                                  ^ reference local11
    } catch (InstantiationException e) {
//           ^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/InstantiationException#
//                                  ^ definition local12
      throw new RuntimeException("Unable to invoke " + constructor, e);
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#`<init>`(+2).
//              ^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#
//                                                     ^^^^^^^^^^^ reference local14
//                                                                  ^ reference local15
    } catch (InvocationTargetException e) {
//           ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/reflect/InvocationTargetException#
//                                     ^ definition local16
      Throwable cause = e.getCause();
//    ^^^^^^^^^ reference java/lang/Throwable#
//              ^^^^^ definition local18
//                      ^ reference local20
//                        ^^^^^^^^ reference java/lang/reflect/InvocationTargetException#getCause().
      if (cause instanceof RuntimeException) {
//        ^^^^^ reference local21
//                         ^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#
        throw (RuntimeException) cause;
//             ^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#
//                               ^^^^^ reference local22
      }
      if (cause instanceof Error) {
//        ^^^^^ reference local23
//                         ^^^^^ reference java/lang/Error#
        throw (Error) cause;
//             ^^^^^ reference java/lang/Error#
//                    ^^^^^ reference local24
      }
      throw new RuntimeException("Unable to get Epoxy helper class.", cause);
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#`<init>`(+2).
//              ^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#
//                                                                    ^^^^^ reference local25
    }
  }

  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  private static Constructor<?> findConstructorForClass(Class<?> controllerClass) {
//               ^^^^^^^^^^^ reference java/lang/reflect/Constructor#
//                              ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ControllerHelperLookup#findConstructorForClass().
//                                                      ^^^^^ reference java/lang/Class#
//                                                               ^^^^^^^^^^^^^^^ definition local26
    Constructor<?> helperCtor = BINDINGS.get(controllerClass);
//  ^^^^^^^^^^^ reference java/lang/reflect/Constructor#
//                 ^^^^^^^^^^ definition local28
//                              ^^^^^^^^ reference com/airbnb/epoxy/ControllerHelperLookup#BINDINGS.
//                                       ^^^ reference java/util/Map#get().
//                                           ^^^^^^^^^^^^^^^ reference local30
    if (helperCtor != null || BINDINGS.containsKey(controllerClass)) {
//      ^^^^^^^^^^ reference local31
//                            ^^^^^^^^ reference com/airbnb/epoxy/ControllerHelperLookup#BINDINGS.
//                                     ^^^^^^^^^^^ reference java/util/Map#containsKey().
//                                                 ^^^^^^^^^^^^^^^ reference local32
      return helperCtor;
//           ^^^^^^^^^^ reference local33
    }

    String clsName = controllerClass.getName();
//  ^^^^^^ reference java/lang/String#
//         ^^^^^^^ definition local34
//                   ^^^^^^^^^^^^^^^ reference local36
//                                   ^^^^^^^ reference java/lang/Class#getName().
    if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
//      ^^^^^^^ reference local37
//              ^^^^^^^^^^ reference java/lang/String#startsWith(+1).
//                                        ^^^^^^^ reference local38
//                                                ^^^^^^^^^^ reference java/lang/String#startsWith(+1).
      return null;
    }

    try {
      Class<?> bindingClass = Class.forName(clsName + GENERATED_HELPER_CLASS_SUFFIX);
//    ^^^^^ reference java/lang/Class#
//             ^^^^^^^^^^^^ definition local39
//                            ^^^^^ reference java/lang/Class#
//                                  ^^^^^^^ reference java/lang/Class#forName().
//                                          ^^^^^^^ reference local41
//                                                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ControllerHelperLookup#GENERATED_HELPER_CLASS_SUFFIX.
      //noinspection unchecked
      helperCtor = bindingClass.getConstructor(controllerClass);
//    ^^^^^^^^^^ reference local42
//                 ^^^^^^^^^^^^ reference local43
//                              ^^^^^^^^^^^^^^ reference java/lang/Class#getConstructor().
//                                             ^^^^^^^^^^^^^^^ reference local44
    } catch (ClassNotFoundException e) {
//           ^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/ClassNotFoundException#
//                                  ^ definition local45
      helperCtor = findConstructorForClass(controllerClass.getSuperclass());
//    ^^^^^^^^^^ reference local47
//                 ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ControllerHelperLookup#findConstructorForClass().
//                                         ^^^^^^^^^^^^^^^ reference local48
//                                                         ^^^^^^^^^^^^^ reference java/lang/Class#getSuperclass().
    } catch (NoSuchMethodException e) {
//           ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/NoSuchMethodException#
//                                 ^ definition local49
      throw new RuntimeException("Unable to find Epoxy Helper constructor for " + clsName, e);
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#`<init>`(+2).
//              ^^^^^^^^^^^^^^^^ reference java/lang/RuntimeException#
//                                                                                ^^^^^^^ reference local51
//                                                                                         ^ reference local52
    }
    BINDINGS.put(controllerClass, helperCtor);
//  ^^^^^^^^ reference com/airbnb/epoxy/ControllerHelperLookup#BINDINGS.
//           ^^^ reference java/util/Map#put().
//               ^^^^^^^^^^^^^^^ reference local53
//                                ^^^^^^^^^^ reference local54
    return helperCtor;
//         ^^^^^^^^^^ reference local55
  }
}

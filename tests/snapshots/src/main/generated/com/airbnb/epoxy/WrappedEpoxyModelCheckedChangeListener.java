package com.airbnb.epoxy;

import android.widget.CompoundButton;
//     ^^^^^^^ reference android/
//             ^^^^^^ reference android/widget/
//                    ^^^^^^^^^^^^^^ reference android/widget/CompoundButton#
import android.widget.CompoundButton.OnCheckedChangeListener;
//     ^^^^^^^ reference android/
//             ^^^^^^ reference android/widget/
//                    ^^^^^^^^^^^^^^ reference android/widget/CompoundButton/
//                                   ^^^^^^^^^^^^^^^^^^^^^^^ reference android/widget/CompoundButton/OnCheckedChangeListener#

import androidx.recyclerview.widget.RecyclerView;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView#

/**
 * Used in the generated models to transform normal checked change listener to model
 * checked change.
 */
public class WrappedEpoxyModelCheckedChangeListener<T extends EpoxyModel<?>, V>
//     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#
//                                                            ^^^^^^^^^^ reference _root_/
    implements OnCheckedChangeListener {
//             ^^^^^^^^^^^^^^^^^^^^^^^ reference _root_/

  private final OnModelCheckedChangeListener<T, V> originalCheckedChangeListener;
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                           ^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#[T]
//                                              ^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#[V]
//                                                 ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#originalCheckedChangeListener.

  public WrappedEpoxyModelCheckedChangeListener(
//       ^^^^^^ definition com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#`<init>`().
      OnModelCheckedChangeListener<T, V> checkedListener
//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference _root_/
//                                 ^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#[T]
//                                    ^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#[V]
//                                       ^^^^^^^^^^^^^^^ definition local0
  ) {
    if (checkedListener == null) {
//      ^^^^^^^^^^^^^^^ reference local2
      throw new IllegalArgumentException("Checked change listener cannot be null");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalArgumentException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalArgumentException#
    }

    this.originalCheckedChangeListener = checkedListener;
//  ^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#this.
//       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#originalCheckedChangeListener.
//                                       ^^^^^^^^^^^^^^^ reference local3
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void onCheckedChanged(CompoundButton button, boolean isChecked) {
//            ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#onCheckedChanged().
//                             ^^^^^^^^^^^^^^ reference _root_/
//                                            ^^^^^^ definition local4
//                                                            ^^^^^^^^^ definition local6
    EpoxyViewHolder epoxyHolder = ListenersUtils.getEpoxyHolderForChildView(button);
//  ^^^^^^^^^^^^^^^ reference _root_/
//                  ^^^^^^^^^^^ definition local8
//                                ^^^^^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference getEpoxyHolderForChildView#
//                                                                          ^^^^^^ reference local10
    if (epoxyHolder == null) {
//      ^^^^^^^^^^^ reference local11
      // Initial binding can trigger the checked changed listener when the checked value is set.
      // The view is not attached at this point so the holder can't be looked up, and in any case
      // it is generally better to not trigger a callback for the binding anyway, since it isn't
      // a user action.
      //
      // https://github.com/airbnb/epoxy/issues/797
      return;
    }

    final int adapterPosition = epoxyHolder.getAdapterPosition();
//            ^^^^^^^^^^^^^^^ definition local12
//                              ^^^^^^^^^^^ reference local14
//                                          ^^^^^^^^^^^^^^^^^^ reference getAdapterPosition#
    if (adapterPosition != RecyclerView.NO_POSITION) {
//      ^^^^^^^^^^^^^^^ reference local15
//                         ^^^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^^^^^ reference NO_POSITION#
      originalCheckedChangeListener
//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#originalCheckedChangeListener.
          .onChecked((T) epoxyHolder.getModel(), (V) epoxyHolder.objectToBind(), button,
//         ^^^^^^^^^ reference `<any>`#onChecked#
//                    ^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#[T]
//                       ^^^^^^^^^^^ reference local16
//                                   ^^^^^^^^ reference getModel#
//                                                ^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#[V]
//                                                   ^^^^^^^^^^^ reference local17
//                                                               ^^^^^^^^^^^^ reference objectToBind#
//                                                                               ^^^^^^ reference local18
          isChecked, adapterPosition);
//        ^^^^^^^^^ reference local19
//                   ^^^^^^^^^^^^^^^ reference local20
    }
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean equals(Object o) {
//               ^^^^^^ definition com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#equals().
//                      ^^^^^^ reference java/lang/Object#
//                             ^ definition local21
    if (this == o) {
//      ^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#this.
//              ^ reference local23
      return true;
    }
    if (!(o instanceof WrappedEpoxyModelCheckedChangeListener)) {
//        ^ reference local24
//                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#
      return false;
    }

    WrappedEpoxyModelCheckedChangeListener<?, ?>
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#
        that = (WrappedEpoxyModelCheckedChangeListener<?, ?>) o;
//      ^^^^ definition local25
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#
//                                                            ^ reference local27

    return originalCheckedChangeListener.equals(that.originalCheckedChangeListener);
//         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#originalCheckedChangeListener.
//                                       ^^^^^^ reference `<any>`#equals#
//                                              ^^^^ reference local28
//                                                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#originalCheckedChangeListener.
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public int hashCode() {
//           ^^^^^^^^ definition com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#hashCode().
    return originalCheckedChangeListener.hashCode();
//         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/WrappedEpoxyModelCheckedChangeListener#originalCheckedChangeListener.
//                                       ^^^^^^^^ reference `<any>`#hashCode#
  }
}

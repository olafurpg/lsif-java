
package com.airbnb.epoxy;

import android.os.Parcel;
//     ^^^^^^^ reference android/
//             ^^ reference android/os/
//                ^^^^^^ reference android/os/Parcel#
import android.os.Parcelable;
//     ^^^^^^^ reference android/
//             ^^ reference android/os/
//                ^^^^^^^^^^ reference android/os/Parcelable#
import android.util.SparseArray;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/util/
//                  ^^^^^^^^^^^ reference android/util/SparseArray#
import android.view.View;
//     ^^^^^^^ reference android/
//             ^^^^ reference android/view/
//                  ^^^^ reference android/view/View#

import com.airbnb.epoxy.ViewHolderState.ViewState;
//     ^^^ reference com/
//         ^^^^^^ reference com/airbnb/
//                ^^^^^ reference com/airbnb/epoxy/
//                      ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#
//                                      ^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#
import com.airbnb.viewmodeladapter.R;
//     ^^^ reference com/
//         ^^^^^^ reference com/airbnb/
//                ^^^^^^^^^^^^^^^^ reference com/airbnb/viewmodeladapter/
//                                 ^ reference com/airbnb/viewmodeladapter/R#

import java.util.Collection;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^ reference java/util/Collection#

import androidx.collection.LongSparseArray;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/collection/
//                         ^^^^^^^^^^^^^^^ reference androidx/collection/LongSparseArray#

/**
 * Helper for {@link EpoxyAdapter} to store the state of Views in the adapter. This is useful for
 * saving changes due to user input, such as text input or selection, when a view is scrolled off
 * screen or if the adapter needs to be recreated.
 * <p/>
 * This saved state is separate from the state represented by a {@link EpoxyModel}, which should
 * represent the more permanent state of the data shown in the view. This class stores transient
 * state that is added to the View after it is bound to a {@link EpoxyModel}. For example, a {@link
 * EpoxyModel} may inflate and bind an EditText and then be responsible for styling it and attaching
 * listeners. If the user then inputs text, scrolls the view offscreen and then scrolls back, this
 * class will preserve the inputted text without the {@link EpoxyModel} needing to be aware of its
 * existence.
 * <p/>
 * This class relies on the adapter having stable ids, as the state of a view is mapped to the id of
 * the {@link EpoxyModel}.
 */
@SuppressWarnings("WeakerAccess")
 ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
class ViewHolderState extends LongSparseArray<ViewState> implements Parcelable {
^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#
//                            ^^^^^^^^^^^^^^^ reference androidx/collection/LongSparseArray#
//                                            ^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#
//                                                                  ^^^^^^^^^^ reference _root_/
  ViewHolderState() {
  ^^^^^^ definition com/airbnb/epoxy/ViewHolderState#`<init>`().
  }

  private ViewHolderState(int size) {
//        ^^^^^^ definition com/airbnb/epoxy/ViewHolderState#`<init>`(+1).
//                            ^^^^ definition local0
    super(size);
//  ^^^^^ reference androidx/collection/LongSparseArray#`<init>`(+1).
//        ^^^^ reference local0
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public int describeContents() {
//           ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#describeContents().
    return 0;
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void writeToParcel(Parcel dest, int flags) {
//            ^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#writeToParcel().
//                          ^^^^^^ reference _root_/
//                                 ^^^^ definition local1
//                                           ^^^^^ definition local2
    final int size = size();
//            ^^^^ definition local3
//                   ^^^^ reference androidx/collection/LongSparseArray#size().
    dest.writeInt(size);
//  ^^^^ reference local1
//       ^^^^^^^^ reference writeInt#
//                ^^^^ reference local3
    for (int i = 0; i < size; i++) {
//           ^ definition local4
//                  ^ reference local4
//                      ^^^^ reference local3
//                            ^ reference local4
      dest.writeLong(keyAt(i));
//    ^^^^ reference local1
//         ^^^^^^^^^ reference writeLong#
//                   ^^^^^ reference androidx/collection/LongSparseArray#keyAt().
//                         ^ reference local4
      dest.writeParcelable(valueAt(i), 0);
//    ^^^^ reference local1
//         ^^^^^^^^^^^^^^^ reference writeParcelable#
//                         ^^^^^^^ reference androidx/collection/LongSparseArray#valueAt().
//                                 ^ reference local4
    }
  }

  public static final Creator<ViewHolderState> CREATOR = new Creator<ViewHolderState>() {
//                    ^^^^^^^ reference _root_/
//                            ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#
//                                             ^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#CREATOR.
//                                                           ^^^^^^^ reference _root_/
//                                                                   ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#

    public ViewHolderState[] newArray(int size) {
      return new ViewHolderState[size];
    }

    public ViewHolderState createFromParcel(Parcel source) {
      int size = source.readInt();
      ViewHolderState state = new ViewHolderState(size);

      for (int i = 0; i < size; i++) {
        long key = source.readLong();
        ViewState value = source.readParcelable(ViewState.class.getClassLoader());
        state.put(key, value);
      }

      return state;
    }
  };

  public boolean hasStateForHolder(EpoxyViewHolder holder) {
//               ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#hasStateForHolder().
//                                 ^^^^^^^^^^^^^^^ reference _root_/
//                                                 ^^^^^^ definition local5
    return get(holder.getItemId()) != null;
//         ^^^ reference androidx/collection/LongSparseArray#get().
//             ^^^^^^ reference local5
//                    ^^^^^^^^^ reference getItemId#
  }

  public void save(Collection<EpoxyViewHolder> holders) {
//            ^^^^ definition com/airbnb/epoxy/ViewHolderState#save().
//                 ^^^^^^^^^^ reference java/util/Collection#
//                            ^^^^^^^^^^^^^^^ reference _root_/
//                                             ^^^^^^^ definition local6
    for (EpoxyViewHolder holder : holders) {
//       ^^^^^^^^^^^^^^^ reference _root_/
//                       ^^^^^^ definition local7
//                                ^^^^^^^ reference local6
      save(holder);
//    ^^^^ reference com/airbnb/epoxy/ViewHolderState#save().
//         ^^^^^^ reference local7
    }
  }

  /** Save the state of the view bound to the given holder. */
  public void save(EpoxyViewHolder holder) {
//            ^^^^ definition com/airbnb/epoxy/ViewHolderState#save(+1).
//                 ^^^^^^^^^^^^^^^ reference _root_/
//                                 ^^^^^^ definition local8
    if (!holder.getModel().shouldSaveViewState()) {
//       ^^^^^^ reference local8
//              ^^^^^^^^ reference getModel#
//                         ^^^^^^^^^^^^^^^^^^^ reference getModel#shouldSaveViewState#
      return;
    }

    // Reuse the previous sparse array if available. We shouldn't need to clear it since the
    // exact same view type is being saved to it, which
    // should have identical ids for all its views, and will just overwrite the previous state.
    ViewState state = get(holder.getItemId());
//  ^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#
//            ^^^^^ definition local9
//                    ^^^ reference androidx/collection/LongSparseArray#get().
//                        ^^^^^^ reference local8
//                               ^^^^^^^^^ reference getItemId#
    if (state == null) {
//      ^^^^^ reference local9
      state = new ViewState();
//    ^^^^^ reference local9
//            ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#`<init>`().
//                ^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#
    }

    state.save(holder.itemView);
//  ^^^^^ reference local9
//        ^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#save().
//             ^^^^^^ reference local8
//                    ^^^^^^^^ reference itemView#
    put(holder.getItemId(), state);
//  ^^^ reference androidx/collection/LongSparseArray#put().
//      ^^^^^^ reference local8
//             ^^^^^^^^^ reference getItemId#
//                          ^^^^^ reference local9
  }

  /**
   * If a state was previously saved for this view holder via {@link #save} it will be restored
   * here.
   */
  public void restore(EpoxyViewHolder holder) {
//            ^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#restore().
//                    ^^^^^^^^^^^^^^^ reference _root_/
//                                    ^^^^^^ definition local10
    if (!holder.getModel().shouldSaveViewState()) {
//       ^^^^^^ reference local10
//              ^^^^^^^^ reference getModel#
//                         ^^^^^^^^^^^^^^^^^^^ reference getModel#shouldSaveViewState#
      return;
    }

    ViewState state = get(holder.getItemId());
//  ^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#
//            ^^^^^ definition local11
//                    ^^^ reference androidx/collection/LongSparseArray#get().
//                        ^^^^^^ reference local10
//                               ^^^^^^^^^ reference getItemId#
    if (state != null) {
//      ^^^^^ reference local11
      state.restore(holder.itemView);
//    ^^^^^ reference local11
//          ^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#restore().
//                  ^^^^^^ reference local10
//                         ^^^^^^^^ reference itemView#
    } else {
      // The first time a model is bound it won't have previous state. We need to make sure
      // the view is reset to its initial state to clear any changes from previously bound models
      holder.restoreInitialViewState();
//    ^^^^^^ reference local10
//           ^^^^^^^^^^^^^^^^^^^^^^^ reference restoreInitialViewState#
    }
  }

  /**
   * A wrapper around a sparse array as a helper to save the state of a view. This also adds
   * parcelable support.
   */
  public static class ViewState extends SparseArray<Parcelable> implements Parcelable {
//              ^^^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#
//                                      ^^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^^^^^ reference _root_/
//                                                                         ^^^^^^^^^^ reference _root_/

    ViewState() {
//  ^^^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#`<init>`().
    }

    private ViewState(int size, int[] keys, Parcelable[] values) {
//          ^^^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#`<init>`(+1).
//                        ^^^^ definition local12
//                                    ^^^^ definition local13
//                                          ^^^^^^^^^^ reference _root_/
//                                                       ^^^^^^ definition local14
      super(size);
//          ^^^^ reference local12
      for (int i = 0; i < size; ++i) {
//             ^ definition local15
//                    ^ reference local15
//                        ^^^^ reference local12
//                                ^ reference local15
        put(keys[i], values[i]);
//      ^^^ reference androidx/collection/LongSparseArray#put().
//          ^^^^ reference local13
//               ^ reference local15
//                   ^^^^^^ reference local14
//                          ^ reference local15
      }
    }

    public void save(View view) {
//              ^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#save().
//                   ^^^^ reference _root_/
//                        ^^^^ definition local16
      int originalId = view.getId();
//        ^^^^^^^^^^ definition local17
//                     ^^^^ reference local16
//                          ^^^^^ reference getId#
      setIdIfNoneExists(view);
//    ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#setIdIfNoneExists().
//                      ^^^^ reference local16

      view.saveHierarchyState(this);
//    ^^^^ reference local16
//         ^^^^^^^^^^^^^^^^^^ reference saveHierarchyState#
//                            ^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#this.
      view.setId(originalId);
//    ^^^^ reference local16
//         ^^^^^ reference setId#
//               ^^^^^^^^^^ reference local17
    }

    public void restore(View view) {
//              ^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#restore().
//                      ^^^^ reference _root_/
//                           ^^^^ definition local18
      int originalId = view.getId();
//        ^^^^^^^^^^ definition local19
//                     ^^^^ reference local18
//                          ^^^^^ reference getId#
      setIdIfNoneExists(view);
//    ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#setIdIfNoneExists().
//                      ^^^^ reference local18

      view.restoreHierarchyState(this);
//    ^^^^ reference local18
//         ^^^^^^^^^^^^^^^^^^^^^ reference restoreHierarchyState#
//                               ^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#this.
      view.setId(originalId);
//    ^^^^ reference local18
//         ^^^^^ reference setId#
//               ^^^^^^^^^^ reference local19
    }

    /**
     * If a view hasn't had an id set we need to set a temporary one in order to save state, since a
     * view won't save its state unless it has an id. The view's id is also the key into the sparse
     * array for its saved state, so the temporary one we choose just needs to be consistent between
     * saving and restoring state.
     */
    private void setIdIfNoneExists(View view) {
//               ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#setIdIfNoneExists().
//                                 ^^^^ reference _root_/
//                                      ^^^^ definition local20
      if (view.getId() == View.NO_ID) {
//        ^^^^ reference local20
//             ^^^^^ reference getId#
//                        ^^^^ reference _root_/
//                             ^^^^^ reference NO_ID#
        view.setId(R.id.view_model_state_saving_id);
//      ^^^^ reference local20
//           ^^^^^ reference setId#
//                 ^ reference R/
//                   ^^ reference R/id#
//                      ^^^^^^^^^^^^^^^^^^^^^^^^^^ reference R/id#view_model_state_saving_id#
      }
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public int describeContents() {
//             ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#describeContents().
      return 0;
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public void writeToParcel(Parcel parcel, int flags) {
//              ^^^^^^^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#writeToParcel().
//                            ^^^^^^ reference _root_/
//                                   ^^^^^^ definition local21
//                                               ^^^^^ definition local22
      int size = size();
//        ^^^^ definition local23
//               ^^^^ reference androidx/collection/LongSparseArray#size().
      int[] keys = new int[size];
//          ^^^^ definition local24
//                         ^^^^ reference local23
      Parcelable[] values = new Parcelable[size];
//    ^^^^^^^^^^ reference _root_/
//                 ^^^^^^ definition local25
//                              ^^^^^^^^^^ reference _root_/
//                                         ^^^^ reference local23
      for (int i = 0; i < size; ++i) {
//             ^ definition local26
//                    ^ reference local26
//                        ^^^^ reference local23
//                                ^ reference local26
        keys[i] = keyAt(i);
//      ^^^^ reference local24
//           ^ reference local26
//                ^^^^^ reference androidx/collection/LongSparseArray#keyAt().
//                      ^ reference local26
        values[i] = valueAt(i);
//      ^^^^^^ reference local25
//             ^ reference local26
//                  ^^^^^^^ reference androidx/collection/LongSparseArray#valueAt().
//                          ^ reference local26
      }
      parcel.writeInt(size);
//    ^^^^^^ reference local21
//           ^^^^^^^^ reference writeInt#
//                    ^^^^ reference local23
      parcel.writeIntArray(keys);
//    ^^^^^^ reference local21
//           ^^^^^^^^^^^^^ reference writeIntArray#
//                         ^^^^ reference local24
      parcel.writeParcelableArray(values, flags);
//    ^^^^^^ reference local21
//           ^^^^^^^^^^^^^^^^^^^^ reference writeParcelableArray#
//                                ^^^^^^ reference local25
//                                        ^^^^^ reference local22
    }

    public static final Creator<ViewState> CREATOR =
//                      ^^^^^^^ reference _root_/
//                              ^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#
//                                         ^^^^^^^ definition com/airbnb/epoxy/ViewHolderState#ViewState#CREATOR.
        new Parcelable.ClassLoaderCreator<ViewState>() {
//          ^^^^^^^^^^ reference Parcelable/
//                     ^^^^^^^^^^^^^^^^^^ reference Parcelable/ClassLoaderCreator#
//                                        ^^^^^^^^^ reference com/airbnb/epoxy/ViewHolderState#ViewState#
          @Override
          public ViewState createFromParcel(Parcel source, ClassLoader loader) {
            int size = source.readInt();
            int[] keys = new int[size];
            source.readIntArray(keys);
            Parcelable[] values = source.readParcelableArray(loader);
            return new ViewState(size, keys, values);
          }

          @Override
          public ViewState createFromParcel(Parcel source) {
            return createFromParcel(source, null);
          }

          @Override
          public ViewState[] newArray(int size) {
            return new ViewState[size];
          }
        };
  }
}

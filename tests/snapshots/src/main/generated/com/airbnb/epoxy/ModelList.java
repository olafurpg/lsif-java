package com.airbnb.epoxy;

import java.util.AbstractList;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^^^ reference java/util/AbstractList#
import java.util.ArrayList;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^ reference java/util/ArrayList#
import java.util.Collection;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^ reference java/util/Collection#
import java.util.ConcurrentModificationException;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
import java.util.Iterator;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^ reference java/util/Iterator#
import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#
import java.util.ListIterator;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^^^ reference java/util/ListIterator#
import java.util.NoSuchElementException;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^^^^^^^^^^^^^ reference java/util/NoSuchElementException#

import androidx.annotation.NonNull;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^ reference androidx/annotation/NonNull#

/**
 * Used by our {@link EpoxyAdapter} to track models. It simply wraps ArrayList and notifies an
 * observer when remove or insertion operations are done on the list. This allows us to optimize
 * diffing since we have a knowledge of what changed in the list.
 */
class ModelList extends ArrayList<EpoxyModel<?>> {
^^^^^^^^^ definition com/airbnb/epoxy/ModelList#
//                      ^^^^^^^^^ reference java/util/ArrayList#
//                                ^^^^^^^^^^ reference _root_/

  ModelList(int expectedModelCount) {
  ^^^^^^ definition com/airbnb/epoxy/ModelList#`<init>`().
//              ^^^^^^^^^^^^^^^^^^ definition local0
    super(expectedModelCount);
//  ^^^^^ reference java/util/ArrayList#`<init>`().
//        ^^^^^^^^^^^^^^^^^^ reference local2
  }

  ModelList() {
  ^^^^^^ definition com/airbnb/epoxy/ModelList#`<init>`(+1).

  }

  interface ModelListObserver {
  ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#ModelListObserver#
    void onItemRangeInserted(int positionStart, int itemCount);
//       ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#ModelListObserver#onItemRangeInserted().
//                               ^^^^^^^^^^^^^ definition local3
//                                                  ^^^^^^^^^ definition local5
    void onItemRangeRemoved(int positionStart, int itemCount);
//       ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#ModelListObserver#onItemRangeRemoved().
//                              ^^^^^^^^^^^^^ definition local7
//                                                 ^^^^^^^^^ definition local9
  }

  private boolean notificationsPaused;
//                ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#notificationsPaused.
  private ModelListObserver observer;
//        ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#ModelListObserver#
//                          ^^^^^^^^ definition com/airbnb/epoxy/ModelList#observer.

  void pauseNotifications() {
//     ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#pauseNotifications().
    if (notificationsPaused) {
//      ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notificationsPaused.
      throw new IllegalStateException("Notifications already paused");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
    }
    notificationsPaused = true;
//  ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notificationsPaused.
  }

  void resumeNotifications() {
//     ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#resumeNotifications().
    if (!notificationsPaused) {
//       ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notificationsPaused.
      throw new IllegalStateException("Notifications already resumed");
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1).
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
    }
    notificationsPaused = false;
//  ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notificationsPaused.
  }

  void setObserver(ModelListObserver observer) {
//     ^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#setObserver().
//                 ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#ModelListObserver#
//                                   ^^^^^^^^ definition local11
    this.observer = observer;
//  ^^^^ reference com/airbnb/epoxy/ModelList#this.
//       ^^^^^^^^ reference com/airbnb/epoxy/ModelList#observer.
//                  ^^^^^^^^ reference local13
  }

  private void notifyInsertion(int positionStart, int itemCount) {
//             ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#notifyInsertion().
//                                 ^^^^^^^^^^^^^ definition local14
//                                                    ^^^^^^^^^ definition local16
    if (!notificationsPaused && observer != null) {
//       ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notificationsPaused.
//                              ^^^^^^^^ reference com/airbnb/epoxy/ModelList#observer.
      observer.onItemRangeInserted(positionStart, itemCount);
//    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#observer.
//             ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#ModelListObserver#onItemRangeInserted().
//                                 ^^^^^^^^^^^^^ reference local18
//                                                ^^^^^^^^^ reference local19
    }
  }

  private void notifyRemoval(int positionStart, int itemCount) {
//             ^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#notifyRemoval().
//                               ^^^^^^^^^^^^^ definition local20
//                                                  ^^^^^^^^^ definition local22
    if (!notificationsPaused && observer != null) {
//       ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notificationsPaused.
//                              ^^^^^^^^ reference com/airbnb/epoxy/ModelList#observer.
      observer.onItemRangeRemoved(positionStart, itemCount);
//    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#observer.
//             ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#ModelListObserver#onItemRangeRemoved().
//                                ^^^^^^^^^^^^^ reference local24
//                                               ^^^^^^^^^ reference local25
    }
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public EpoxyModel<?> set(int index, EpoxyModel<?> element) {
//       ^^^^^^^^^^ reference _root_/
//                     ^^^ definition com/airbnb/epoxy/ModelList#set().
//                             ^^^^^ definition local26
//                                    ^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^^ definition local28
    EpoxyModel<?> previousModel = super.set(index, element);
//  ^^^^^^^^^^ reference _root_/
//                ^^^^^^^^^^^^^ definition local30
//                                ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//                                      ^^^ reference java/util/ArrayList#set().
//                                          ^^^^^ reference local32
//                                                 ^^^^^^^ reference local33

    if (previousModel.id() != element.id()) {
//      ^^^^^^^^^^^^^ reference local34
//                    ^^ reference `<any>`#id#
//                            ^^^^^^^ reference local35
//                                    ^^ reference `<any>`#id#
      notifyRemoval(index, 1);
//    ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyRemoval().
//                  ^^^^^ reference local36
      notifyInsertion(index, 1);
//    ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyInsertion().
//                    ^^^^^ reference local37
    }

    return previousModel;
//         ^^^^^^^^^^^^^ reference local38
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean add(EpoxyModel<?> epoxyModel) {
//               ^^^ definition com/airbnb/epoxy/ModelList#add().
//                   ^^^^^^^^^^ reference _root_/
//                                 ^^^^^^^^^^ definition local39
    notifyInsertion(size(), 1);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyInsertion().
//                  ^^^^ reference java/util/ArrayList#size(+1).
    return super.add(epoxyModel);
//         ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//               ^^^ reference java/util/ArrayList#add().
//                   ^^^^^^^^^^ reference local41
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void add(int index, EpoxyModel<?> element) {
//            ^^^ definition com/airbnb/epoxy/ModelList#add(+1).
//                    ^^^^^ definition local42
//                           ^^^^^^^^^^ reference _root_/
//                                         ^^^^^^^ definition local44
    notifyInsertion(index, 1);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyInsertion().
//                  ^^^^^ reference local46
    super.add(index, element);
//  ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//        ^^^ reference java/util/ArrayList#add(+1).
//            ^^^^^ reference local47
//                   ^^^^^^^ reference local48
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean addAll(Collection<? extends EpoxyModel<?>> c) {
//               ^^^^^^ definition com/airbnb/epoxy/ModelList#addAll().
//                      ^^^^^^^^^^ reference java/util/Collection#
//                                           ^^^^^^^^^^ reference _root_/
//                                                          ^ definition local49
    notifyInsertion(size(), c.size());
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyInsertion().
//                  ^^^^ reference java/util/ArrayList#size(+1).
//                          ^ reference local51
//                            ^^^^ reference java/util/Collection#size().
    return super.addAll(c);
//         ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//               ^^^^^^ reference java/util/ArrayList#addAll().
//                      ^ reference local52
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean addAll(int index, Collection<? extends EpoxyModel<?>> c) {
//               ^^^^^^ definition com/airbnb/epoxy/ModelList#addAll(+1).
//                          ^^^^^ definition local53
//                                 ^^^^^^^^^^ reference java/util/Collection#
//                                                      ^^^^^^^^^^ reference _root_/
//                                                                     ^ definition local55
    notifyInsertion(index, c.size());
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyInsertion().
//                  ^^^^^ reference local57
//                         ^ reference local58
//                           ^^^^ reference java/util/Collection#size().
    return super.addAll(index, c);
//         ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//               ^^^^^^ reference java/util/ArrayList#addAll(+1).
//                      ^^^^^ reference local59
//                             ^ reference local60
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public EpoxyModel<?> remove(int index) {
//       ^^^^^^^^^^ reference _root_/
//                     ^^^^^^ definition com/airbnb/epoxy/ModelList#remove().
//                                ^^^^^ definition local61
    notifyRemoval(index, 1);
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyRemoval().
//                ^^^^^ reference local63
    return super.remove(index);
//         ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//               ^^^^^^ reference java/util/ArrayList#remove().
//                      ^^^^^ reference local64
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean remove(Object o) {
//               ^^^^^^ definition com/airbnb/epoxy/ModelList#remove(+1).
//                      ^^^^^^ reference java/lang/Object#
//                             ^ definition local65
    int index = indexOf(o);
//      ^^^^^ definition local67
//              ^^^^^^^ reference java/util/ArrayList#indexOf().
//                      ^ reference local69

    if (index == -1) {
//      ^^^^^ reference local70
      return false;
    }

    notifyRemoval(index, 1);
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyRemoval().
//                ^^^^^ reference local71
    super.remove(index);
//  ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//        ^^^^^^ reference java/util/ArrayList#remove().
//               ^^^^^ reference local72
    return true;
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public void clear() {
//            ^^^^^ definition com/airbnb/epoxy/ModelList#clear().
    if (!isEmpty()) {
//       ^^^^^^^ reference java/util/ArrayList#isEmpty().
      notifyRemoval(0, size());
//    ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyRemoval().
//                     ^^^^ reference java/util/ArrayList#size(+1).
      super.clear();
//    ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//          ^^^^^ reference java/util/ArrayList#clear().
    }
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  protected void removeRange(int fromIndex, int toIndex) {
//               ^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#removeRange().
//                               ^^^^^^^^^ definition local73
//                                              ^^^^^^^ definition local75
    if (fromIndex == toIndex) {
//      ^^^^^^^^^ reference local77
//                   ^^^^^^^ reference local78
      return;
    }

    notifyRemoval(fromIndex, toIndex - fromIndex);
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#notifyRemoval().
//                ^^^^^^^^^ reference local79
//                           ^^^^^^^ reference local80
//                                     ^^^^^^^^^ reference local81
    super.removeRange(fromIndex, toIndex);
//  ^^^^^ reference com/airbnb/epoxy/ModelList#super.
//        ^^^^^^^^^^^ reference java/util/ArrayList#removeRange().
//                    ^^^^^^^^^ reference local82
//                               ^^^^^^^ reference local83
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean removeAll(Collection<?> collection) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/ModelList#removeAll().
//                         ^^^^^^^^^^ reference java/util/Collection#
//                                       ^^^^^^^^^^ definition local84
    // Using this implementation from the Android ArrayList since the Java 1.8 ArrayList
    // doesn't call through to remove. Calling through to remove lets us leverage the notification
    // done there
    boolean result = false;
//          ^^^^^^ definition local86
    Iterator<?> it = iterator();
//  ^^^^^^^^ reference java/util/Iterator#
//              ^^ definition local88
//                   ^^^^^^^^ reference com/airbnb/epoxy/ModelList#iterator().
    while (it.hasNext()) {
//         ^^ reference local90
//            ^^^^^^^ reference java/util/Iterator#hasNext().
      if (collection.contains(it.next())) {
//        ^^^^^^^^^^ reference local91
//                   ^^^^^^^^ reference java/util/Collection#contains().
//                            ^^ reference local92
//                               ^^^^ reference java/util/Iterator#next().
        it.remove();
//      ^^ reference local93
//         ^^^^^^ reference java/util/Iterator#remove().
        result = true;
//      ^^^^^^ reference local94
      }
    }
    return result;
//         ^^^^^^ reference local95
  }

  @Override
   ^^^^^^^^ reference java/lang/Override#
  public boolean retainAll(Collection<?> collection) {
//               ^^^^^^^^^ definition com/airbnb/epoxy/ModelList#retainAll().
//                         ^^^^^^^^^^ reference java/util/Collection#
//                                       ^^^^^^^^^^ definition local96
    // Using this implementation from the Android ArrayList since the Java 1.8 ArrayList
    // doesn't call through to remove. Calling through to remove lets us leverage the notification
    // done there
    boolean result = false;
//          ^^^^^^ definition local98
    Iterator<?> it = iterator();
//  ^^^^^^^^ reference java/util/Iterator#
//              ^^ definition local100
//                   ^^^^^^^^ reference com/airbnb/epoxy/ModelList#iterator().
    while (it.hasNext()) {
//         ^^ reference local102
//            ^^^^^^^ reference java/util/Iterator#hasNext().
      if (!collection.contains(it.next())) {
//         ^^^^^^^^^^ reference local103
//                    ^^^^^^^^ reference java/util/Collection#contains().
//                             ^^ reference local104
//                                ^^^^ reference java/util/Iterator#next().
        it.remove();
//      ^^ reference local105
//         ^^^^^^ reference java/util/Iterator#remove().
        result = true;
//      ^^^^^^ reference local106
      }
    }
    return result;
//         ^^^^^^ reference local107
  }

  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public Iterator<EpoxyModel<?>> iterator() {
//       ^^^^^^^^ reference java/util/Iterator#
//                ^^^^^^^^^^ reference _root_/
//                               ^^^^^^^^ definition com/airbnb/epoxy/ModelList#iterator().
    return new Itr();
//         ^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#`<init>`().
//             ^^^ reference com/airbnb/epoxy/ModelList#Itr#
  }

  /**
   * An Iterator implementation that calls through to the parent list's methods for modification.
   * Some implementations, like the Android ArrayList.ArrayListIterator class, modify the list data
   * directly instead of calling into the parent list's methods. We need the implementation to call
   * the parent methods so that the proper notifications are done.
   */
  private class Itr implements Iterator<EpoxyModel<?>> {
//        ^^^ definition com/airbnb/epoxy/ModelList#Itr#
//        ^^^^^^ definition com/airbnb/epoxy/ModelList#Itr#`<init>`().
//                             ^^^^^^^^ reference java/util/Iterator#
//                                      ^^^^^^^^^^ reference _root_/
    int cursor;       // index of next element to return
//      ^^^^^^ definition com/airbnb/epoxy/ModelList#Itr#cursor.
    int lastRet = -1; // index of last element returned; -1 if no such
//      ^^^^^^^ definition com/airbnb/epoxy/ModelList#Itr#lastRet.
    int expectedModCount = modCount;
//      ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#Itr#expectedModCount.
//                         ^^^^^^^^ reference java/util/AbstractList#modCount.

    public boolean hasNext() {
//                 ^^^^^^^ definition com/airbnb/epoxy/ModelList#Itr#hasNext().
      return cursor != size();
//           ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
//                     ^^^^ reference java/util/ArrayList#size(+1).
    }

    @SuppressWarnings("unchecked")
//   ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
    public EpoxyModel<?> next() {
//         ^^^^^^^^^^ reference _root_/
//                       ^^^^ definition com/airbnb/epoxy/ModelList#Itr#next().
      checkForComodification();
//    ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#checkForComodification().
      int i = cursor;
//        ^ definition local108
//            ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
      cursor = i + 1;
//    ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
//             ^ reference local110
      lastRet = i;
//    ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
//              ^ reference local111
      return ModelList.this.get(i);
//           ^^^^^^^^^ reference com/airbnb/epoxy/ModelList#
//                     ^^^^ reference com/airbnb/epoxy/ModelList#this.
//                          ^^^ reference java/util/ArrayList#get().
//                              ^ reference local112
    }

    public void remove() {
//              ^^^^^^ definition com/airbnb/epoxy/ModelList#Itr#remove().
      if (lastRet < 0) {
//        ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
        throw new IllegalStateException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
      }
      checkForComodification();
//    ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#checkForComodification().

      try {
        ModelList.this.remove(lastRet);
//      ^^^^^^^^^ reference com/airbnb/epoxy/ModelList#
//                ^^^^ reference com/airbnb/epoxy/ModelList#this.
//                     ^^^^^^ reference com/airbnb/epoxy/ModelList#remove().
//                            ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
        cursor = lastRet;
//      ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
//               ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
        lastRet = -1;
//      ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
        expectedModCount = modCount;
//      ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#expectedModCount.
//                         ^^^^^^^^ reference java/util/AbstractList#modCount.
      } catch (IndexOutOfBoundsException ex) {
//             ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
//                                       ^^ definition local113
        throw new ConcurrentModificationException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
      }
    }

    final void checkForComodification() {
//             ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#Itr#checkForComodification().
      if (modCount != expectedModCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#expectedModCount.
        throw new ConcurrentModificationException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
      }
    }
  }

  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public ListIterator<EpoxyModel<?>> listIterator() {
//       ^^^^^^^^^^^^ reference java/util/ListIterator#
//                    ^^^^^^^^^^ reference _root_/
//                                   ^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#listIterator().
    return new ListItr(0);
//         ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#ListItr#`<init>`().
//             ^^^^^^^ reference com/airbnb/epoxy/ModelList#ListItr#
  }

  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public ListIterator<EpoxyModel<?>> listIterator(int index) {
//       ^^^^^^^^^^^^ reference java/util/ListIterator#
//                    ^^^^^^^^^^ reference _root_/
//                                   ^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#listIterator(+1).
//                                                    ^^^^^ definition local115
    return new ListItr(index);
//         ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#ListItr#`<init>`().
//             ^^^^^^^ reference com/airbnb/epoxy/ModelList#ListItr#
//                     ^^^^^ reference local117
  }

  /**
   * A ListIterator implementation that calls through to the parent list's methods for modification.
   * Some implementations may modify the list data directly instead of calling into the parent
   * list's methods. We need the implementation to call the parent methods so that the proper
   * notifications are done.
   */
  private class ListItr extends Itr implements ListIterator<EpoxyModel<?>> {
//        ^^^^^^^ definition com/airbnb/epoxy/ModelList#ListItr#
//                              ^^^ reference com/airbnb/epoxy/ModelList#Itr#
//                                             ^^^^^^^^^^^^ reference java/util/ListIterator#
//                                                          ^^^^^^^^^^ reference _root_/
    ListItr(int index) {
//  ^^^^^^ definition com/airbnb/epoxy/ModelList#ListItr#`<init>`().
//              ^^^^^ definition local118
      cursor = index;
//    ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
//             ^^^^^ reference local120
    }

    public boolean hasPrevious() {
//                 ^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#ListItr#hasPrevious().
      return cursor != 0;
//           ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
    }

    public int nextIndex() {
//             ^^^^^^^^^ definition com/airbnb/epoxy/ModelList#ListItr#nextIndex().
      return cursor;
//           ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
    }

    public int previousIndex() {
//             ^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#ListItr#previousIndex().
      return cursor - 1;
//           ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
    }

    @SuppressWarnings("unchecked")
//   ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
    public EpoxyModel<?> previous() {
//         ^^^^^^^^^^ reference _root_/
//                       ^^^^^^^^ definition com/airbnb/epoxy/ModelList#ListItr#previous().
      checkForComodification();
//    ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#checkForComodification().
      int i = cursor - 1;
//        ^ definition local121
//            ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
      if (i < 0) {
//        ^ reference local123
        throw new NoSuchElementException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/NoSuchElementException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^ reference java/util/NoSuchElementException#
      }

      cursor = i;
//    ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
//             ^ reference local124
      lastRet = i;
//    ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
//              ^ reference local125
      return ModelList.this.get(i);
//           ^^^^^^^^^ reference com/airbnb/epoxy/ModelList#
//                     ^^^^ reference com/airbnb/epoxy/ModelList#this.
//                          ^^^ reference java/util/ArrayList#get().
//                              ^ reference local126
    }

    public void set(EpoxyModel<?> e) {
//              ^^^ definition com/airbnb/epoxy/ModelList#ListItr#set().
//                  ^^^^^^^^^^ reference _root_/
//                                ^ definition local127
      if (lastRet < 0) {
//        ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
        throw new IllegalStateException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
      }
      checkForComodification();
//    ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#checkForComodification().

      try {
        ModelList.this.set(lastRet, e);
//      ^^^^^^^^^ reference com/airbnb/epoxy/ModelList#
//                ^^^^ reference com/airbnb/epoxy/ModelList#this.
//                     ^^^ reference com/airbnb/epoxy/ModelList#set().
//                         ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
//                                  ^ reference local129
      } catch (IndexOutOfBoundsException ex) {
//             ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
//                                       ^^ definition local130
        throw new ConcurrentModificationException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
      }
    }

    public void add(EpoxyModel<?> e) {
//              ^^^ definition com/airbnb/epoxy/ModelList#ListItr#add().
//                  ^^^^^^^^^^ reference _root_/
//                                ^ definition local132
      checkForComodification();
//    ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#checkForComodification().

      try {
        int i = cursor;
//          ^ definition local134
//              ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
        ModelList.this.add(i, e);
//      ^^^^^^^^^ reference com/airbnb/epoxy/ModelList#
//                ^^^^ reference com/airbnb/epoxy/ModelList#this.
//                     ^^^ reference com/airbnb/epoxy/ModelList#add(+1).
//                         ^ reference local136
//                            ^ reference local137
        cursor = i + 1;
//      ^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#cursor.
//               ^ reference local138
        lastRet = -1;
//      ^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#lastRet.
        expectedModCount = modCount;
//      ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#Itr#expectedModCount.
//                         ^^^^^^^^ reference java/util/AbstractList#modCount.
      } catch (IndexOutOfBoundsException ex) {
//             ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
//                                       ^^ definition local139
        throw new ConcurrentModificationException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
      }
    }
  }

  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  @Override
   ^^^^^^^^ reference java/lang/Override#
  public List<EpoxyModel<?>> subList(int start, int end) {
//       ^^^^ reference java/util/List#
//            ^^^^^^^^^^ reference _root_/
//                           ^^^^^^^ definition com/airbnb/epoxy/ModelList#subList().
//                                       ^^^^^ definition local141
//                                                  ^^^ definition local143
    if (start >= 0 && end <= size()) {
//      ^^^^^ reference local145
//                    ^^^ reference local146
//                           ^^^^ reference java/util/ArrayList#size(+1).
      if (start <= end) {
//        ^^^^^ reference local147
//                 ^^^ reference local148
        return new SubList(this, start, end);
//             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#`<init>`().
//                 ^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#
//                         ^^^^ reference com/airbnb/epoxy/ModelList#this.
//                               ^^^^^ reference local149
//                                      ^^^ reference local150
      }
      throw new IllegalArgumentException();
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalArgumentException#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalArgumentException#
    }
    throw new IndexOutOfBoundsException();
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#`<init>`().
//            ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
  }

  /**
   * A SubList implementation from Android's AbstractList class. It's copied here to make sure the
   * implementation doesn't change, since some implementations, like the Java 1.8 ArrayList.SubList
   * class, modify the list data directly instead of calling into the parent list's methods. We need
   * the implementation to call the parent methods so that the proper notifications are done.
   */
  private static class SubList extends AbstractList<EpoxyModel<?>> {
//               ^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#
//                                     ^^^^^^^^^^^^ reference java/util/AbstractList#
//                                                  ^^^^^^^^^^ reference _root_/
    private final ModelList fullList;
//                ^^^^^^^^^ reference com/airbnb/epoxy/ModelList#
//                          ^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#fullList.
    private int offset;
//              ^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#offset.
    private int size;
//              ^^^^ definition com/airbnb/epoxy/ModelList#SubList#size.

    private static final class SubListIterator implements ListIterator<EpoxyModel<?>> {
//                       ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#
//                                                        ^^^^^^^^^^^^ reference java/util/ListIterator#
//                                                                     ^^^^^^^^^^ reference _root_/
      private final SubList subList;
//                  ^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#
//                          ^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#subList.
      private final ListIterator<EpoxyModel<?>> iterator;
//                  ^^^^^^^^^^^^ reference java/util/ListIterator#
//                               ^^^^^^^^^^ reference _root_/
//                                              ^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
      private int start;
//                ^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#start.
      private int end;
//                ^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#end.

      SubListIterator(ListIterator<EpoxyModel<?>> it, SubList list, int offset, int length) {
//    ^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#`<init>`().
//                    ^^^^^^^^^^^^ reference java/util/ListIterator#
//                                 ^^^^^^^^^^ reference _root_/
//                                                ^^ definition local151
//                                                    ^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#
//                                                            ^^^^ definition local153
//                                                                      ^^^^^^ definition local155
//                                                                                  ^^^^^^ definition local157
        iterator = it;
//      ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                 ^^ reference local159
        subList = list;
//      ^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#subList.
//                ^^^^ reference local160
        start = offset;
//      ^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#start.
//              ^^^^^^ reference local161
        end = start + length;
//      ^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#end.
//            ^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#start.
//                    ^^^^^^ reference local162
      }

      public void add(EpoxyModel<?> object) {
//                ^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#add().
//                    ^^^^^^^^^^ reference _root_/
//                                  ^^^^^^ definition local163
        iterator.add(object);
//      ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//               ^^^ reference java/util/ListIterator#add().
//                   ^^^^^^ reference local165
        subList.sizeChanged(true);
//      ^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#subList.
//              ^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#sizeChanged().
        end++;
//      ^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#end.
      }

      public boolean hasNext() {
//                   ^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#hasNext().
        return iterator.nextIndex() < end;
//             ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                      ^^^^^^^^^ reference java/util/ListIterator#nextIndex().
//                                    ^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#end.
      }

      public boolean hasPrevious() {
//                   ^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#hasPrevious().
        return iterator.previousIndex() >= start;
//             ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                      ^^^^^^^^^^^^^ reference java/util/ListIterator#previousIndex().
//                                         ^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#start.
      }

      public EpoxyModel<?> next() {
//           ^^^^^^^^^^ reference _root_/
//                         ^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#next().
        if (iterator.nextIndex() < end) {
//          ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                   ^^^^^^^^^ reference java/util/ListIterator#nextIndex().
//                                 ^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#end.
          return iterator.next();
//               ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                        ^^^^ reference java/util/ListIterator#next().
        }
        throw new NoSuchElementException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/NoSuchElementException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^ reference java/util/NoSuchElementException#
      }

      public int nextIndex() {
//               ^^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#nextIndex().
        return iterator.nextIndex() - start;
//             ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                      ^^^^^^^^^ reference java/util/ListIterator#nextIndex().
//                                    ^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#start.
      }

      public EpoxyModel<?> previous() {
//           ^^^^^^^^^^ reference _root_/
//                         ^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#previous().
        if (iterator.previousIndex() >= start) {
//          ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                   ^^^^^^^^^^^^^ reference java/util/ListIterator#previousIndex().
//                                      ^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#start.
          return iterator.previous();
//               ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                        ^^^^^^^^ reference java/util/ListIterator#previous().
        }
        throw new NoSuchElementException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/NoSuchElementException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^ reference java/util/NoSuchElementException#
      }

      public int previousIndex() {
//               ^^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#previousIndex().
        int previous = iterator.previousIndex();
//          ^^^^^^^^ definition local166
//                     ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//                              ^^^^^^^^^^^^^ reference java/util/ListIterator#previousIndex().
        if (previous >= start) {
//          ^^^^^^^^ reference local168
//                      ^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#start.
          return previous - start;
//               ^^^^^^^^ reference local169
//                          ^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#start.
        }
        return -1;
      }

      public void remove() {
//                ^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#remove().
        iterator.remove();
//      ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//               ^^^^^^ reference java/util/ListIterator#remove().
        subList.sizeChanged(false);
//      ^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#subList.
//              ^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#sizeChanged().
        end--;
//      ^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#end.
      }

      public void set(EpoxyModel<?> object) {
//                ^^^ definition com/airbnb/epoxy/ModelList#SubList#SubListIterator#set().
//                    ^^^^^^^^^^ reference _root_/
//                                  ^^^^^^ definition local170
        iterator.set(object);
//      ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#iterator.
//               ^^^ reference java/util/ListIterator#set().
//                   ^^^^^^ reference local172
      }
    }

    SubList(ModelList list, int start, int end) {
//  ^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#`<init>`().
//          ^^^^^^^^^ reference com/airbnb/epoxy/ModelList#
//                    ^^^^ definition local173
//                              ^^^^^ definition local175
//                                         ^^^ definition local177
      fullList = list;
//    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//               ^^^^ reference local179
      modCount = fullList.modCount;
//    ^^^^^^^^ reference java/util/AbstractList#modCount.
//               ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                        ^^^^^^^^ reference java/util/AbstractList#modCount.
      offset = start;
//    ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
//             ^^^^^ reference local180
      size = end - start;
//    ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
//           ^^^ reference local181
//                 ^^^^^ reference local182
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public void add(int location, EpoxyModel<?> object) {
//              ^^^ definition com/airbnb/epoxy/ModelList#SubList#add().
//                      ^^^^^^^^ definition local183
//                                ^^^^^^^^^^ reference _root_/
//                                              ^^^^^^ definition local185
      if (modCount == fullList.modCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                             ^^^^^^^^ reference java/util/AbstractList#modCount.
        if (location >= 0 && location <= size) {
//          ^^^^^^^^ reference local187
//                           ^^^^^^^^ reference local188
//                                       ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
          fullList.add(location + offset, object);
//        ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                 ^^^ reference com/airbnb/epoxy/ModelList#add(+1).
//                     ^^^^^^^^ reference local189
//                                ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
//                                        ^^^^^^ reference local190
          size++;
//        ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
          modCount = fullList.modCount;
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                   ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                            ^^^^^^^^ reference java/util/AbstractList#modCount.
        } else {
          throw new IndexOutOfBoundsException();
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#`<init>`().
//                  ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
        }
      } else {
        throw new ConcurrentModificationException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
      }
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public boolean addAll(int location, Collection<? extends EpoxyModel<?>> collection) {
//                 ^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#addAll().
//                            ^^^^^^^^ definition local191
//                                      ^^^^^^^^^^ reference java/util/Collection#
//                                                           ^^^^^^^^^^ reference _root_/
//                                                                          ^^^^^^^^^^ definition local193
      if (modCount == fullList.modCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                             ^^^^^^^^ reference java/util/AbstractList#modCount.
        if (location >= 0 && location <= size) {
//          ^^^^^^^^ reference local195
//                           ^^^^^^^^ reference local196
//                                       ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
          boolean result = fullList.addAll(location + offset, collection);
//                ^^^^^^ definition local197
//                         ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                                  ^^^^^^ reference com/airbnb/epoxy/ModelList#addAll(+1).
//                                         ^^^^^^^^ reference local199
//                                                    ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
//                                                            ^^^^^^^^^^ reference local200
          if (result) {
//            ^^^^^^ reference local201
            size += collection.size();
//          ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
//                  ^^^^^^^^^^ reference local202
//                             ^^^^ reference java/util/Collection#size().
            modCount = fullList.modCount;
//          ^^^^^^^^ reference java/util/AbstractList#modCount.
//                     ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                              ^^^^^^^^ reference java/util/AbstractList#modCount.
          }
          return result;
//               ^^^^^^ reference local203
        }
        throw new IndexOutOfBoundsException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
      }
      throw new ConcurrentModificationException();
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public boolean addAll(@NonNull Collection<? extends EpoxyModel<?>> collection) {
//                 ^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#addAll(+1).
//                         ^^^^^^^ reference androidx/annotation/NonNull#
//                                 ^^^^^^^^^^ reference java/util/Collection#
//                                                      ^^^^^^^^^^ reference _root_/
//                                                                     ^^^^^^^^^^ definition local204
      if (modCount == fullList.modCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                             ^^^^^^^^ reference java/util/AbstractList#modCount.
        boolean result = fullList.addAll(offset + size, collection);
//              ^^^^^^ definition local206
//                       ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                                ^^^^^^ reference com/airbnb/epoxy/ModelList#addAll(+1).
//                                       ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
//                                                ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
//                                                      ^^^^^^^^^^ reference local208
        if (result) {
//          ^^^^^^ reference local209
          size += collection.size();
//        ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
//                ^^^^^^^^^^ reference local210
//                           ^^^^ reference java/util/Collection#size().
          modCount = fullList.modCount;
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                   ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                            ^^^^^^^^ reference java/util/AbstractList#modCount.
        }
        return result;
//             ^^^^^^ reference local211
      }
      throw new ConcurrentModificationException();
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public EpoxyModel<?> get(int location) {
//         ^^^^^^^^^^ reference _root_/
//                       ^^^ definition com/airbnb/epoxy/ModelList#SubList#get().
//                               ^^^^^^^^ definition local212
      if (modCount == fullList.modCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                             ^^^^^^^^ reference java/util/AbstractList#modCount.
        if (location >= 0 && location < size) {
//          ^^^^^^^^ reference local214
//                           ^^^^^^^^ reference local215
//                                      ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
          return fullList.get(location + offset);
//               ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                        ^^^ reference java/util/ArrayList#get().
//                            ^^^^^^^^ reference local216
//                                       ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
        }
        throw new IndexOutOfBoundsException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
      }
      throw new ConcurrentModificationException();
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
    }

    @NonNull
//   ^^^^^^^ reference androidx/annotation/NonNull#
    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public Iterator<EpoxyModel<?>> iterator() {
//         ^^^^^^^^ reference java/util/Iterator#
//                  ^^^^^^^^^^ reference _root_/
//                                 ^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#iterator().
      return listIterator(0);
//           ^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#listIterator().
    }

    @NonNull
//   ^^^^^^^ reference androidx/annotation/NonNull#
    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public ListIterator<EpoxyModel<?>> listIterator(int location) {
//         ^^^^^^^^^^^^ reference java/util/ListIterator#
//                      ^^^^^^^^^^ reference _root_/
//                                     ^^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#listIterator().
//                                                      ^^^^^^^^ definition local217
      if (modCount == fullList.modCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                             ^^^^^^^^ reference java/util/AbstractList#modCount.
        if (location >= 0 && location <= size) {
//          ^^^^^^^^ reference local219
//                           ^^^^^^^^ reference local220
//                                       ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
          return new SubListIterator(fullList.listIterator(location + offset), this, offset, size);
//               ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#`<init>`().
//                   ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#SubListIterator#
//                                   ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                                            ^^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#listIterator(+1).
//                                                         ^^^^^^^^ reference local221
//                                                                    ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
//                                                                             ^^^^ reference com/airbnb/epoxy/ModelList#SubList#this.
//                                                                                   ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
//                                                                                           ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
        }
        throw new IndexOutOfBoundsException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
      }
      throw new ConcurrentModificationException();
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public EpoxyModel<?> remove(int location) {
//         ^^^^^^^^^^ reference _root_/
//                       ^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#remove().
//                                  ^^^^^^^^ definition local222
      if (modCount == fullList.modCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                             ^^^^^^^^ reference java/util/AbstractList#modCount.
        if (location >= 0 && location < size) {
//          ^^^^^^^^ reference local224
//                           ^^^^^^^^ reference local225
//                                      ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
          EpoxyModel<?> result = fullList.remove(location + offset);
//        ^^^^^^^^^^ reference _root_/
//                      ^^^^^^ definition local226
//                               ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                                        ^^^^^^ reference com/airbnb/epoxy/ModelList#remove().
//                                               ^^^^^^^^ reference local228
//                                                          ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
          size--;
//        ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
          modCount = fullList.modCount;
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                   ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                            ^^^^^^^^ reference java/util/AbstractList#modCount.
          return result;
//               ^^^^^^ reference local229
        }
        throw new IndexOutOfBoundsException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
      }
      throw new ConcurrentModificationException();
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    protected void removeRange(int start, int end) {
//                 ^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#removeRange().
//                                 ^^^^^ definition local230
//                                            ^^^ definition local232
      if (start != end) {
//        ^^^^^ reference local234
//                 ^^^ reference local235
        if (modCount == fullList.modCount) {
//          ^^^^^^^^ reference java/util/AbstractList#modCount.
//                      ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                               ^^^^^^^^ reference java/util/AbstractList#modCount.
          fullList.removeRange(start + offset, end + offset);
//        ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                 ^^^^^^^^^^^ reference com/airbnb/epoxy/ModelList#removeRange().
//                             ^^^^^ reference local236
//                                     ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
//                                             ^^^ reference local237
//                                                   ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
          size -= end - start;
//        ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
//                ^^^ reference local238
//                      ^^^^^ reference local239
          modCount = fullList.modCount;
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                   ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                            ^^^^^^^^ reference java/util/AbstractList#modCount.
        } else {
          throw new ConcurrentModificationException();
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
        }
      }
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public EpoxyModel<?> set(int location, EpoxyModel<?> object) {
//         ^^^^^^^^^^ reference _root_/
//                       ^^^ definition com/airbnb/epoxy/ModelList#SubList#set().
//                               ^^^^^^^^ definition local240
//                                         ^^^^^^^^^^ reference _root_/
//                                                       ^^^^^^ definition local242
      if (modCount == fullList.modCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                             ^^^^^^^^ reference java/util/AbstractList#modCount.
        if (location >= 0 && location < size) {
//          ^^^^^^^^ reference local244
//                           ^^^^^^^^ reference local245
//                                      ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
          return fullList.set(location + offset, object);
//               ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                        ^^^ reference com/airbnb/epoxy/ModelList#set().
//                            ^^^^^^^^ reference local246
//                                       ^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#offset.
//                                               ^^^^^^ reference local247
        }
        throw new IndexOutOfBoundsException();
//            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#`<init>`().
//                ^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IndexOutOfBoundsException#
      }
      throw new ConcurrentModificationException();
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public int size() {
//             ^^^^ definition com/airbnb/epoxy/ModelList#SubList#size(+1).
      if (modCount == fullList.modCount) {
//        ^^^^^^^^ reference java/util/AbstractList#modCount.
//                    ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                             ^^^^^^^^ reference java/util/AbstractList#modCount.
        return size;
//             ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
      }
      throw new ConcurrentModificationException();
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#`<init>`().
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/util/ConcurrentModificationException#
    }

    void sizeChanged(boolean increment) {
//       ^^^^^^^^^^^ definition com/airbnb/epoxy/ModelList#SubList#sizeChanged().
//                           ^^^^^^^^^ definition local248
      if (increment) {
//        ^^^^^^^^^ reference local250
        size++;
//      ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
      } else {
        size--;
//      ^^^^ reference com/airbnb/epoxy/ModelList#SubList#size.
      }
      modCount = fullList.modCount;
//    ^^^^^^^^ reference java/util/AbstractList#modCount.
//               ^^^^^^^^ reference com/airbnb/epoxy/ModelList#SubList#fullList.
//                        ^^^^^^^^ reference java/util/AbstractList#modCount.
    }
  }
}


package com.airbnb.epoxy;

import java.util.ArrayList;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^ reference java/util/ArrayList#
import java.util.HashMap;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^ reference java/util/HashMap#
import java.util.Iterator;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^ reference java/util/Iterator#
import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#
import java.util.Map;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^ reference java/util/Map#

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#
import androidx.recyclerview.widget.RecyclerView;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^^^^^ reference androidx/recyclerview/widget/RecyclerView#

/**
 * Helper to track changes in the models list.
 */
class DiffHelper {
^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#
  private ArrayList<ModelState> oldStateList = new ArrayList<>();
//        ^^^^^^^^^ reference java/util/ArrayList#
//                  ^^^^^^^^^^ reference _root_/
//                              ^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#oldStateList.
//                                             ^^^^^^^^^^^^^^^^^ reference java/util/ArrayList#`<init>`(+1).
//                                                 ^^^^^^^^^ reference java/util/ArrayList#
  // Using a HashMap instead of a LongSparseArray to
  // have faster look up times at the expense of memory
  private Map<Long, ModelState> oldStateMap = new HashMap<>();
//        ^^^ reference java/util/Map#
//            ^^^^ reference java/lang/Long#
//                  ^^^^^^^^^^ reference _root_/
//                              ^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#oldStateMap.
//                                            ^^^^^^^^^^^^^^^ reference java/util/HashMap#`<init>`(+2).
//                                                ^^^^^^^ reference java/util/HashMap#
  private ArrayList<ModelState> currentStateList = new ArrayList<>();
//        ^^^^^^^^^ reference java/util/ArrayList#
//                  ^^^^^^^^^^ reference _root_/
//                              ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#currentStateList.
//                                                 ^^^^^^^^^^^^^^^^^ reference java/util/ArrayList#`<init>`(+1).
//                                                     ^^^^^^^^^ reference java/util/ArrayList#
  private Map<Long, ModelState> currentStateMap = new HashMap<>();
//        ^^^ reference java/util/Map#
//            ^^^^ reference java/lang/Long#
//                  ^^^^^^^^^^ reference _root_/
//                              ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#currentStateMap.
//                                                ^^^^^^^^^^^^^^^ reference java/util/HashMap#`<init>`(+2).
//                                                    ^^^^^^^ reference java/util/HashMap#
  private final BaseEpoxyAdapter adapter;
//              ^^^^^^^^^^^^^^^^ reference _root_/
//                               ^^^^^^^ definition com/airbnb/epoxy/DiffHelper#adapter.
  private final boolean immutableModels;
//                      ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#immutableModels.


  DiffHelper(BaseEpoxyAdapter adapter, boolean immutableModels) {
  ^^^^^^ definition com/airbnb/epoxy/DiffHelper#`<init>`().
//           ^^^^^^^^^^^^^^^^ reference _root_/
//                            ^^^^^^^ definition local0
//                                             ^^^^^^^^^^^^^^^ definition local2
    this.adapter = adapter;
//  ^^^^ reference com/airbnb/epoxy/DiffHelper#this.
//       ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                 ^^^^^^^ reference local4
    this.immutableModels = immutableModels;
//  ^^^^ reference com/airbnb/epoxy/DiffHelper#this.
//       ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#immutableModels.
//                         ^^^^^^^^^^^^^^^ reference local5
    adapter.registerAdapterDataObserver(observer);
//  ^^^^^^^ reference local6
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference registerAdapterDataObserver#
//                                      ^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#observer.
  }

  private final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
//              ^^^^^^^^^^^^ reference RecyclerView/
//                           ^^^^^^^^^^^^^^^^^^^ reference RecyclerView/AdapterDataObserver#
//                                               ^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#observer.
//                                                              ^^^^^^^^^^^^ reference RecyclerView/
//                                                                           ^^^^^^^^^^^^^^^^^^^ reference RecyclerView/AdapterDataObserver#
    @Override
    public void onChanged() {
      throw new UnsupportedOperationException(
          "Diffing is enabled. You should use notifyModelsChanged instead of notifyDataSetChanged");
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
      for (int i = positionStart; i < positionStart + itemCount; i++) {
        currentStateList.get(i).hashCode = adapter.getCurrentModels().get(i).hashCode();
      }
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
      if (itemCount == 0) {
        // no-op
        return;
      }

      if (itemCount == 1 || positionStart == currentStateList.size()) {
        for (int i = positionStart; i < positionStart + itemCount; i++) {
          currentStateList.add(i, createStateForPosition(i));
        }
      } else {
        // Add in a batch since multiple insertions to the middle of the list are slow
        List<ModelState> newModels = new ArrayList<>(itemCount);
        for (int i = positionStart; i < positionStart + itemCount; i++) {
          newModels.add(createStateForPosition(i));
        }

        currentStateList.addAll(positionStart, newModels);
      }

      // Update positions of affected items
      int size = currentStateList.size();
      for (int i = positionStart + itemCount; i < size; i++) {
        currentStateList.get(i).position += itemCount;
      }
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
      if (itemCount == 0) {
        // no-op
        return;
      }

      List<ModelState> modelsToRemove =
          currentStateList.subList(positionStart, positionStart + itemCount);
      for (ModelState model : modelsToRemove) {
        currentStateMap.remove(model.id);
      }
      modelsToRemove.clear();

      // Update positions of affected items
      int size = currentStateList.size();
      for (int i = positionStart; i < size; i++) {
        currentStateList.get(i).position -= itemCount;
      }
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
      if (fromPosition == toPosition) {
        // no-op
        return;
      }

      if (itemCount != 1) {
        throw new IllegalArgumentException("Moving more than 1 item at a time is not "
            + "supported. Number of items moved: " + itemCount);
      }

      ModelState model = currentStateList.remove(fromPosition);
      model.position = toPosition;
      currentStateList.add(toPosition, model);

      if (fromPosition < toPosition) {
        // shift the affected items left
        for (int i = fromPosition; i < toPosition; i++) {
          currentStateList.get(i).position--;
        }
      } else {
        // shift the affected items right
        for (int i = toPosition + 1; i <= fromPosition; i++) {
          currentStateList.get(i).position++;
        }
      }
    }
  };

  /**
   * Set the current list of models. The diff callbacks will be notified of the changes between the
   * current list and the last list that was set.
   */
  void notifyModelChanges() {
//     ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#notifyModelChanges().
    UpdateOpHelper updateOpHelper = new UpdateOpHelper();
//  ^^^^^^^^^^^^^^ reference _root_/
//                 ^^^^^^^^^^^^^^ definition local7
//                                      ^^^^^^^^^^^^^^ reference _root_/

    buildDiff(updateOpHelper);
//  ^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#buildDiff().
//            ^^^^^^^^^^^^^^ reference local9

    // Send out the proper notify calls for the diff. We remove our
    // observer first so that we don't react to our own notify calls
    adapter.unregisterAdapterDataObserver(observer);
//  ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference unregisterAdapterDataObserver#
//                                        ^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#observer.
    notifyChanges(updateOpHelper);
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#notifyChanges().
//                ^^^^^^^^^^^^^^ reference local10
    adapter.registerAdapterDataObserver(observer);
//  ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference registerAdapterDataObserver#
//                                      ^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#observer.
  }

  private void notifyChanges(UpdateOpHelper opHelper) {
//             ^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#notifyChanges().
//                           ^^^^^^^^^^^^^^ reference _root_/
//                                          ^^^^^^^^ definition local11
    for (UpdateOp op : opHelper.opList) {
//       ^^^^^^^^ reference _root_/
//                ^^ definition local13
//                     ^^^^^^^^ reference local15
//                              ^^^^^^ reference opList#
      switch (op.type) {
//            ^^ reference local16
//               ^^^^ reference type#
        case UpdateOp.ADD:
//           ^^^^^^^^ reference _root_/
//                    ^^^ reference ADD#
          adapter.notifyItemRangeInserted(op.positionStart, op.itemCount);
//        ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                ^^^^^^^^^^^^^^^^^^^^^^^ reference notifyItemRangeInserted#
//                                        ^^ reference local17
//                                           ^^^^^^^^^^^^^ reference positionStart#
//                                                          ^^ reference local18
//                                                             ^^^^^^^^^ reference itemCount#
          break;
        case UpdateOp.MOVE:
//           ^^^^^^^^ reference _root_/
//                    ^^^^ reference MOVE#
          adapter.notifyItemMoved(op.positionStart, op.itemCount);
//        ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                ^^^^^^^^^^^^^^^ reference notifyItemMoved#
//                                ^^ reference local19
//                                   ^^^^^^^^^^^^^ reference positionStart#
//                                                  ^^ reference local20
//                                                     ^^^^^^^^^ reference itemCount#
          break;
        case UpdateOp.REMOVE:
//           ^^^^^^^^ reference _root_/
//                    ^^^^^^ reference REMOVE#
          adapter.notifyItemRangeRemoved(op.positionStart, op.itemCount);
//        ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                ^^^^^^^^^^^^^^^^^^^^^^ reference notifyItemRangeRemoved#
//                                       ^^ reference local21
//                                          ^^^^^^^^^^^^^ reference positionStart#
//                                                         ^^ reference local22
//                                                            ^^^^^^^^^ reference itemCount#
          break;
        case UpdateOp.UPDATE:
//           ^^^^^^^^ reference _root_/
//                    ^^^^^^ reference UPDATE#
          if (immutableModels && op.payloads != null) {
//            ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#immutableModels.
//                               ^^ reference local23
//                                  ^^^^^^^^ reference payloads#
            adapter.notifyItemRangeChanged(op.positionStart, op.itemCount,
//          ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                  ^^^^^^^^^^^^^^^^^^^^^^ reference notifyItemRangeChanged#
//                                         ^^ reference local24
//                                            ^^^^^^^^^^^^^ reference positionStart#
//                                                           ^^ reference local25
//                                                              ^^^^^^^^^ reference itemCount#
                new DiffPayload(op.payloads));
//                  ^^^^^^^^^^^ reference _root_/
//                              ^^ reference local26
//                                 ^^^^^^^^ reference payloads#
          } else {
            adapter.notifyItemRangeChanged(op.positionStart, op.itemCount);
//          ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                  ^^^^^^^^^^^^^^^^^^^^^^ reference notifyItemRangeChanged#
//                                         ^^ reference local27
//                                            ^^^^^^^^^^^^^ reference positionStart#
//                                                           ^^ reference local28
//                                                              ^^^^^^^^^ reference itemCount#
          }
          break;
        default:
          throw new IllegalArgumentException("Unknown type: " + op.type);
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalArgumentException#`<init>`(+1).
//                  ^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalArgumentException#
//                                                              ^^ reference local29
//                                                                 ^^^^ reference type#
      }
    }
  }

  /**
   * Create a list of operations that define the difference between {@link #oldStateList} and {@link
   * #currentStateList}.
   */
  private UpdateOpHelper buildDiff(UpdateOpHelper updateOpHelper) {
//        ^^^^^^^^^^^^^^ reference _root_/
//                       ^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#buildDiff().
//                                 ^^^^^^^^^^^^^^ reference _root_/
//                                                ^^^^^^^^^^^^^^ definition local30
    prepareStateForDiff();
//  ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#prepareStateForDiff().

    // The general approach is to first search for removals, then additions, and lastly changes.
    // Focusing on one type of operation at a time makes it easy to coalesce batch changes.
    // When we identify an operation and add it to the
    // result list we update the positions of items in the oldStateList to reflect
    // the change, this way subsequent operations will use the correct, updated positions.
    collectRemovals(updateOpHelper);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#collectRemovals().
//                  ^^^^^^^^^^^^^^ reference local32

    // Only need to check for insertions if new list is bigger
    boolean hasInsertions =
//          ^^^^^^^^^^^^^ definition local33
        oldStateList.size() - updateOpHelper.getNumRemovals() != currentStateList.size();
//      ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
//                   ^^^^ reference java/util/ArrayList#size(+1).
//                            ^^^^^^^^^^^^^^ reference local35
//                                           ^^^^^^^^^^^^^^ reference getNumRemovals#
//                                                               ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateList.
//                                                                                ^^^^ reference java/util/ArrayList#size(+1).
    if (hasInsertions) {
//      ^^^^^^^^^^^^^ reference local36
      collectInsertions(updateOpHelper);
//    ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#collectInsertions().
//                      ^^^^^^^^^^^^^^ reference local37
    }

    collectMoves(updateOpHelper);
//  ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#collectMoves().
//               ^^^^^^^^^^^^^^ reference local38
    collectChanges(updateOpHelper);
//  ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#collectChanges().
//                 ^^^^^^^^^^^^^^ reference local39

    resetOldState();
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#resetOldState().

    return updateOpHelper;
//         ^^^^^^^^^^^^^^ reference local40
  }

  private void resetOldState() {
//             ^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#resetOldState().
    oldStateList.clear();
//  ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
//               ^^^^^ reference java/util/ArrayList#clear().
    oldStateMap.clear();
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateMap.
//              ^^^^^ reference java/util/Map#clear().
  }

  private void prepareStateForDiff() {
//             ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#prepareStateForDiff().
    // We use a list of the models as well as a map by their id,
    // so we can easily find them by both position and id

    oldStateList.clear();
//  ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
//               ^^^^^ reference java/util/ArrayList#clear().
    oldStateMap.clear();
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateMap.
//              ^^^^^ reference java/util/Map#clear().

    // Swap the two lists so that we have a copy of the current state to calculate the next diff
    ArrayList<ModelState> tempList = oldStateList;
//  ^^^^^^^^^ reference java/util/ArrayList#
//            ^^^^^^^^^^ reference _root_/
//                        ^^^^^^^^ definition local41
//                                   ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
    oldStateList = currentStateList;
//  ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
//                 ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateList.
    currentStateList = tempList;
//  ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateList.
//                     ^^^^^^^^ reference local43

    Map<Long, ModelState> tempMap = oldStateMap;
//  ^^^ reference java/util/Map#
//      ^^^^ reference java/lang/Long#
//            ^^^^^^^^^^ reference _root_/
//                        ^^^^^^^ definition local44
//                                  ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateMap.
    oldStateMap = currentStateMap;
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateMap.
//                ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateMap.
    currentStateMap = tempMap;
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateMap.
//                    ^^^^^^^ reference local46

    // Remove all pairings in the old states so we can tell which of them were removed. The items
    // that still exist in the new list will be paired when we build the current list state below
    for (ModelState modelState : oldStateList) {
//       ^^^^^^^^^^ reference _root_/
//                  ^^^^^^^^^^ definition local47
//                               ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
      modelState.pair = null;
//    ^^^^^^^^^^ reference local49
//               ^^^^ reference pair#
    }

    int modelCount = adapter.getCurrentModels().size();
//      ^^^^^^^^^^ definition local50
//                   ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                           ^^^^^^^^^^^^^^^^ reference getCurrentModels#
//                                              ^^^^ reference getCurrentModels#size#
    currentStateList.ensureCapacity(modelCount);
//  ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateList.
//                   ^^^^^^^^^^^^^^ reference java/util/ArrayList#ensureCapacity().
//                                  ^^^^^^^^^^ reference local52

    for (int i = 0; i < modelCount; i++) {
//           ^ definition local53
//                  ^ reference local55
//                      ^^^^^^^^^^ reference local56
//                                  ^ reference local57
      currentStateList.add(createStateForPosition(i));
//    ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateList.
//                     ^^^ reference java/util/ArrayList#add().
//                         ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#createStateForPosition().
//                                                ^ reference local58
    }
  }

  private ModelState createStateForPosition(int position) {
//        ^^^^^^^^^^ reference _root_/
//                   ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#createStateForPosition().
//                                              ^^^^^^^^ definition local59
    EpoxyModel<?> model = adapter.getCurrentModels().get(position);
//  ^^^^^^^^^^ reference _root_/
//                ^^^^^ definition local61
//                        ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                                ^^^^^^^^^^^^^^^^ reference getCurrentModels#
//                                                   ^^^ reference getCurrentModels#get#
//                                                       ^^^^^^^^ reference local63
    model.addedToAdapter = true;
//  ^^^^^ reference local64
//        ^^^^^^^^^^^^^^ reference `<any>`#addedToAdapter#
    ModelState state = ModelState.build(model, position, immutableModels);
//  ^^^^^^^^^^ reference _root_/
//             ^^^^^ definition local65
//                     ^^^^^^^^^^ reference _root_/
//                                ^^^^^ reference build#
//                                      ^^^^^ reference local67
//                                             ^^^^^^^^ reference local68
//                                                       ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#immutableModels.

    ModelState previousValue = currentStateMap.put(state.id, state);
//  ^^^^^^^^^^ reference _root_/
//             ^^^^^^^^^^^^^ definition local69
//                             ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateMap.
//                                             ^^^ reference java/util/Map#put().
//                                                 ^^^^^ reference local71
//                                                       ^^ reference id#
//                                                           ^^^^^ reference local72
    if (previousValue != null) {
//      ^^^^^^^^^^^^^ reference local73
      int previousPosition = previousValue.position;
//        ^^^^^^^^^^^^^^^^ definition local74
//                           ^^^^^^^^^^^^^ reference local76
//                                         ^^^^^^^^ reference position#
      EpoxyModel<?> previousModel = adapter.getCurrentModels().get(previousPosition);
//    ^^^^^^^^^^ reference _root_/
//                  ^^^^^^^^^^^^^ definition local77
//                                  ^^^^^^^ reference com/airbnb/epoxy/DiffHelper#adapter.
//                                          ^^^^^^^^^^^^^^^^ reference getCurrentModels#
//                                                             ^^^ reference getCurrentModels#get#
//                                                                 ^^^^^^^^^^^^^^^^ reference local79
      throw new IllegalStateException("Two models have the same ID. ID's must be unique!"
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#`<init>`(+1). 2:76
//              ^^^^^^^^^^^^^^^^^^^^^ reference java/lang/IllegalStateException#
          + " Model at position " + position + ": " + model
//                                  ^^^^^^^^ reference local80
//                                                    ^^^^^ reference local81
          + " Model at position " + previousPosition + ": " + previousModel);
//                                  ^^^^^^^^^^^^^^^^ reference local82
//                                                            ^^^^^^^^^^^^^ reference local83
    }

    return state;
//         ^^^^^ reference local84
  }

  /**
   * Find all removal operations and add them to the result list. The general strategy here is to
   * walk through the {@link #oldStateList} and check for items that don't exist in the new list.
   * Walking through it in order makes it easy to batch adjacent removals.
   */
  private void collectRemovals(UpdateOpHelper helper) {
//             ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#collectRemovals().
//                             ^^^^^^^^^^^^^^ reference _root_/
//                                            ^^^^^^ definition local85
    for (ModelState state : oldStateList) {
//       ^^^^^^^^^^ reference _root_/
//                  ^^^^^ definition local87
//                          ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
      // Update the position of the item to take into account previous removals,
      // so that future operations will reference the correct position
      state.position -= helper.getNumRemovals();
//    ^^^^^ reference local89
//          ^^^^^^^^ reference position#
//                      ^^^^^^ reference local90
//                             ^^^^^^^^^^^^^^ reference getNumRemovals#

      // This is our first time going through the list, so we
      // look up the item with the matching id in the new
      // list and hold a reference to it so that we can access it quickly in the future
      state.pair = currentStateMap.get(state.id);
//    ^^^^^ reference local91
//          ^^^^ reference pair#
//                 ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateMap.
//                                 ^^^ reference java/util/Map#get().
//                                     ^^^^^ reference local92
//                                           ^^ reference id#
      if (state.pair != null) {
//        ^^^^^ reference local93
//              ^^^^ reference pair#
        state.pair.pair = state;
//      ^^^^^ reference local94
//            ^^^^ reference pair#
//                 ^^^^ reference pair#pair#
//                        ^^^^^ reference local95
        continue;
      }

      helper.remove(state.position);
//    ^^^^^^ reference local96
//           ^^^^^^ reference remove#
//                  ^^^^^ reference local97
//                        ^^^^^^^^ reference position#
    }
  }

  /**
   * Find all insertion operations and add them to the result list. The general strategy here is to
   * walk through the {@link #currentStateList} and check for items that don't exist in the old
   * list. Walking through it in order makes it easy to batch adjacent insertions.
   */
  private void collectInsertions(UpdateOpHelper helper) {
//             ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#collectInsertions().
//                               ^^^^^^^^^^^^^^ reference _root_/
//                                              ^^^^^^ definition local98
    Iterator<ModelState> oldItemIterator = oldStateList.iterator();
//  ^^^^^^^^ reference java/util/Iterator#
//           ^^^^^^^^^^ reference _root_/
//                       ^^^^^^^^^^^^^^^ definition local100
//                                         ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
//                                                      ^^^^^^^^ reference java/util/ArrayList#iterator().

    for (ModelState itemToInsert : currentStateList) {
//       ^^^^^^^^^^ reference _root_/
//                  ^^^^^^^^^^^^ definition local102
//                                 ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateList.
      if (itemToInsert.pair != null) {
//        ^^^^^^^^^^^^ reference local104
//                     ^^^^ reference pair#
        // Update the position of the next item in the old list to take any insertions into account
        ModelState nextOldItem = getNextItemWithPair(oldItemIterator);
//      ^^^^^^^^^^ reference _root_/
//                 ^^^^^^^^^^^ definition local105
//                               ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#getNextItemWithPair().
//                                                   ^^^^^^^^^^^^^^^ reference local107
        if (nextOldItem != null) {
//          ^^^^^^^^^^^ reference local108
          nextOldItem.position += helper.getNumInsertions();
//        ^^^^^^^^^^^ reference local109
//                    ^^^^^^^^ reference position#
//                                ^^^^^^ reference local110
//                                       ^^^^^^^^^^^^^^^^ reference getNumInsertions#
        }
        continue;
      }

      helper.add(itemToInsert.position);
//    ^^^^^^ reference local111
//           ^^^ reference add#
//               ^^^^^^^^^^^^ reference local112
//                            ^^^^^^^^ reference position#
    }
  }

  /**
   * Check if any items have had their values changed, batching if possible.
   */
  private void collectChanges(UpdateOpHelper helper) {
//             ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#collectChanges().
//                            ^^^^^^^^^^^^^^ reference _root_/
//                                           ^^^^^^ definition local113
    for (ModelState newItem : currentStateList) {
//       ^^^^^^^^^^ reference _root_/
//                  ^^^^^^^ definition local115
//                            ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateList.
      ModelState previousItem = newItem.pair;
//    ^^^^^^^^^^ reference _root_/
//               ^^^^^^^^^^^^ definition local117
//                              ^^^^^^^ reference local119
//                                      ^^^^ reference pair#
      if (previousItem == null) {
//        ^^^^^^^^^^^^ reference local120
        continue;
      }

      // We use equals when we know the models are immutable and available, otherwise we have to
      // rely on the stored hashCode
      boolean modelChanged;
//            ^^^^^^^^^^^^ definition local121
      if (immutableModels) {
//        ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#immutableModels.
        // Make sure that the old model hasn't changed, otherwise comparing it with the new one
        // won't be accurate.
        if (previousItem.model.isDebugValidationEnabled()) {
//          ^^^^^^^^^^^^ reference local123
//                       ^^^^^ reference model#
//                             ^^^^^^^^^^^^^^^^^^^^^^^^ reference model#isDebugValidationEnabled#
          previousItem.model
//        ^^^^^^^^^^^^ reference local124
//                     ^^^^^ reference model#
              .validateStateHasNotChangedSinceAdded("Model was changed before it could be diffed.",
//             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference model#validateStateHasNotChangedSinceAdded#
                  previousItem.position);
//                ^^^^^^^^^^^^ reference local125
//                             ^^^^^^^^ reference position#
        }

        modelChanged = !previousItem.model.equals(newItem.model);
//      ^^^^^^^^^^^^ reference local126
//                      ^^^^^^^^^^^^ reference local127
//                                   ^^^^^ reference model#
//                                         ^^^^^^ reference model#equals#
//                                                ^^^^^^^ reference local128
//                                                        ^^^^^ reference model#
      } else {
        modelChanged = previousItem.hashCode != newItem.hashCode;
//      ^^^^^^^^^^^^ reference local129
//                     ^^^^^^^^^^^^ reference local130
//                                  ^^^^^^^^ reference hashCode#
//                                              ^^^^^^^ reference local131
//                                                      ^^^^^^^^ reference hashCode#
      }

      if (modelChanged) {
//        ^^^^^^^^^^^^ reference local132
        helper.update(newItem.position, previousItem.model);
//      ^^^^^^ reference local133
//             ^^^^^^ reference update#
//                    ^^^^^^^ reference local134
//                            ^^^^^^^^ reference position#
//                                      ^^^^^^^^^^^^ reference local135
//                                                   ^^^^^ reference model#
      }
    }
  }

  /**
   * Check which items have had a position changed. Recyclerview does not support batching these.
   */
  private void collectMoves(UpdateOpHelper helper) {
//             ^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#collectMoves().
//                          ^^^^^^^^^^^^^^ reference _root_/
//                                         ^^^^^^ definition local136
    // This walks through both the new and old list simultaneous and checks for position changes.
    Iterator<ModelState> oldItemIterator = oldStateList.iterator();
//  ^^^^^^^^ reference java/util/Iterator#
//           ^^^^^^^^^^ reference _root_/
//                       ^^^^^^^^^^^^^^^ definition local138
//                                         ^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#oldStateList.
//                                                      ^^^^^^^^ reference java/util/ArrayList#iterator().
    ModelState nextOldItem = null;
//  ^^^^^^^^^^ reference _root_/
//             ^^^^^^^^^^^ definition local140

    for (ModelState newItem : currentStateList) {
//       ^^^^^^^^^^ reference _root_/
//                  ^^^^^^^ definition local142
//                            ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#currentStateList.
      if (newItem.pair == null) {
//        ^^^^^^^ reference local144
//                ^^^^ reference pair#
        // This item was inserted. However, insertions are done at the item's final position, and
        // aren't smart about inserting at a different position to take future moves into account.
        // As the old state list is updated to reflect moves, it needs to also consider insertions
        // affected by those moves in order for the final change set to be correct
        if (helper.moves.isEmpty()) {
//          ^^^^^^ reference local145
//                 ^^^^^ reference moves#
//                       ^^^^^^^ reference moves#isEmpty#
          // There have been no moves, so the item is still at it's correct position
          continue;
        } else {
          // There have been moves, so the old list needs to take this inserted item
          // into account. The old list doesn't have this item inserted into it
          // (for optimization purposes), but we can create a pair for this item to
          // track its position in the old list and move it back to its final position if necessary
          newItem.pairWithSelf();
//        ^^^^^^^ reference local146
//                ^^^^^^^^^^^^ reference pairWithSelf#
        }
      }

      // We could iterate through only the new list and move each
      // item that is out of place, however in cases such as moving the first item
      // to the end, that strategy would do many moves to move all
      // items up one instead of doing one move to move the first item to the end.
      // To avoid this we compare the old item to the new item at
      // each index and move the one that is farthest from its correct position.
      // We only move on from a new item once its pair is placed in
      // the correct spot. Since we move from start to end, all new items we've
      // already iterated through are guaranteed to have their pair
      // be already in the right spot, which won't be affected by future MOVEs.
      if (nextOldItem == null) {
//        ^^^^^^^^^^^ reference local147
        nextOldItem = getNextItemWithPair(oldItemIterator);
//      ^^^^^^^^^^^ reference local148
//                    ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#getNextItemWithPair().
//                                        ^^^^^^^^^^^^^^^ reference local149

        // We've already iterated through all old items and moved each
        // item once. However, subsequent moves may have shifted an item out of
        // its correct space once it was already moved. We finish
        // iterating through all the new items to ensure everything is still correct
        if (nextOldItem == null) {
//          ^^^^^^^^^^^ reference local150
          nextOldItem = newItem.pair;
//        ^^^^^^^^^^^ reference local151
//                      ^^^^^^^ reference local152
//                              ^^^^ reference pair#
        }
      }

      while (nextOldItem != null) {
//           ^^^^^^^^^^^ reference local153
        // Make sure the positions are updated to the latest
        // move operations before we calculate the next move
        updateItemPosition(newItem.pair, helper.moves);
//      ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#updateItemPosition().
//                         ^^^^^^^ reference local154
//                                 ^^^^ reference pair#
//                                       ^^^^^^ reference local155
//                                              ^^^^^ reference moves#
        updateItemPosition(nextOldItem, helper.moves);
//      ^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#updateItemPosition().
//                         ^^^^^^^^^^^ reference local156
//                                      ^^^^^^ reference local157
//                                             ^^^^^ reference moves#

        // The item is the same and its already in the correct place
        if (newItem.id == nextOldItem.id && newItem.position == nextOldItem.position) {
//          ^^^^^^^ reference local158
//                  ^^ reference id#
//                        ^^^^^^^^^^^ reference local159
//                                    ^^ reference id#
//                                          ^^^^^^^ reference local160
//                                                  ^^^^^^^^ reference position#
//                                                              ^^^^^^^^^^^ reference local161
//                                                                          ^^^^^^^^ reference position#
          nextOldItem = null;
//        ^^^^^^^^^^^ reference local162
          break;
        }

        int newItemDistance = newItem.pair.position - newItem.position;
//          ^^^^^^^^^^^^^^^ definition local163
//                            ^^^^^^^ reference local165
//                                    ^^^^ reference pair#
//                                         ^^^^^^^^ reference pair#position#
//                                                    ^^^^^^^ reference local166
//                                                            ^^^^^^^^ reference position#
        int oldItemDistance = nextOldItem.pair.position - nextOldItem.position;
//          ^^^^^^^^^^^^^^^ definition local167
//                            ^^^^^^^^^^^ reference local169
//                                        ^^^^ reference pair#
//                                             ^^^^^^^^ reference pair#position#
//                                                        ^^^^^^^^^^^ reference local170
//                                                                    ^^^^^^^^ reference position#

        // Both items are already in the correct position
        if (newItemDistance == 0 && oldItemDistance == 0) {
//          ^^^^^^^^^^^^^^^ reference local171
//                                  ^^^^^^^^^^^^^^^ reference local172
          nextOldItem = null;
//        ^^^^^^^^^^^ reference local173
          break;
        }

        if (oldItemDistance > newItemDistance) {
//          ^^^^^^^^^^^^^^^ reference local174
//                            ^^^^^^^^^^^^^^^ reference local175
          helper.move(nextOldItem.position, nextOldItem.pair.position);
//        ^^^^^^ reference local176
//               ^^^^ reference move#
//                    ^^^^^^^^^^^ reference local177
//                                ^^^^^^^^ reference position#
//                                          ^^^^^^^^^^^ reference local178
//                                                      ^^^^ reference pair#
//                                                           ^^^^^^^^ reference pair#position#

          nextOldItem.position = nextOldItem.pair.position;
//        ^^^^^^^^^^^ reference local179
//                    ^^^^^^^^ reference position#
//                               ^^^^^^^^^^^ reference local180
//                                           ^^^^ reference pair#
//                                                ^^^^^^^^ reference pair#position#
          nextOldItem.lastMoveOp = helper.getNumMoves();
//        ^^^^^^^^^^^ reference local181
//                    ^^^^^^^^^^ reference lastMoveOp#
//                                 ^^^^^^ reference local182
//                                        ^^^^^^^^^^^ reference getNumMoves#

          nextOldItem = getNextItemWithPair(oldItemIterator);
//        ^^^^^^^^^^^ reference local183
//                      ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/DiffHelper#getNextItemWithPair().
//                                          ^^^^^^^^^^^^^^^ reference local184
        } else {
          helper.move(newItem.pair.position, newItem.position);
//        ^^^^^^ reference local185
//               ^^^^ reference move#
//                    ^^^^^^^ reference local186
//                            ^^^^ reference pair#
//                                 ^^^^^^^^ reference pair#position#
//                                           ^^^^^^^ reference local187
//                                                   ^^^^^^^^ reference position#

          newItem.pair.position = newItem.position;
//        ^^^^^^^ reference local188
//                ^^^^ reference pair#
//                     ^^^^^^^^ reference pair#position#
//                                ^^^^^^^ reference local189
//                                        ^^^^^^^^ reference position#
          newItem.pair.lastMoveOp = helper.getNumMoves();
//        ^^^^^^^ reference local190
//                ^^^^ reference pair#
//                     ^^^^^^^^^^ reference pair#lastMoveOp#
//                                  ^^^^^^ reference local191
//                                         ^^^^^^^^^^^ reference getNumMoves#
          break;
        }
      }
    }
  }

  /**
   * Apply the movement operations to the given item to update its position. Only applies the
   * operations that have not been applied yet, and stores how many operations have been applied so
   * we know which ones to apply next time.
   */
  private void updateItemPosition(ModelState item, List<UpdateOp> moveOps) {
//             ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#updateItemPosition().
//                                ^^^^^^^^^^ reference _root_/
//                                           ^^^^ definition local192
//                                                 ^^^^ reference java/util/List#
//                                                      ^^^^^^^^ reference _root_/
//                                                                ^^^^^^^ definition local194
    int size = moveOps.size();
//      ^^^^ definition local196
//             ^^^^^^^ reference local198
//                     ^^^^ reference java/util/List#size().

    for (int i = item.lastMoveOp; i < size; i++) {
//           ^ definition local199
//               ^^^^ reference local201
//                    ^^^^^^^^^^ reference lastMoveOp#
//                                ^ reference local202
//                                    ^^^^ reference local203
//                                          ^ reference local204
      UpdateOp moveOp = moveOps.get(i);
//    ^^^^^^^^ reference _root_/
//             ^^^^^^ definition local205
//                      ^^^^^^^ reference local207
//                              ^^^ reference java/util/List#get().
//                                  ^ reference local208
      int fromPosition = moveOp.positionStart;
//        ^^^^^^^^^^^^ definition local209
//                       ^^^^^^ reference local211
//                              ^^^^^^^^^^^^^ reference positionStart#
      int toPosition = moveOp.itemCount;
//        ^^^^^^^^^^ definition local212
//                     ^^^^^^ reference local214
//                            ^^^^^^^^^ reference itemCount#

      if (item.position > fromPosition && item.position <= toPosition) {
//        ^^^^ reference local215
//             ^^^^^^^^ reference position#
//                        ^^^^^^^^^^^^ reference local216
//                                        ^^^^ reference local217
//                                             ^^^^^^^^ reference position#
//                                                         ^^^^^^^^^^ reference local218
        item.position--;
//      ^^^^ reference local219
//           ^^^^^^^^ reference position#
      } else if (item.position < fromPosition && item.position >= toPosition) {
//               ^^^^ reference local220
//                    ^^^^^^^^ reference position#
//                               ^^^^^^^^^^^^ reference local221
//                                               ^^^^ reference local222
//                                                    ^^^^^^^^ reference position#
//                                                                ^^^^^^^^^^ reference local223
        item.position++;
//      ^^^^ reference local224
//           ^^^^^^^^ reference position#
      }
    }

    item.lastMoveOp = size;
//  ^^^^ reference local225
//       ^^^^^^^^^^ reference lastMoveOp#
//                    ^^^^ reference local226
  }

  /**
   * Gets the next item in the list that has a pair, meaning it wasn't inserted or removed.
   */
  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  private ModelState getNextItemWithPair(Iterator<ModelState> iterator) {
//        ^^^^^^^^^^ reference _root_/
//                   ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/DiffHelper#getNextItemWithPair().
//                                       ^^^^^^^^ reference java/util/Iterator#
//                                                ^^^^^^^^^^ reference _root_/
//                                                            ^^^^^^^^ definition local227
    ModelState nextItem = null;
//  ^^^^^^^^^^ reference _root_/
//             ^^^^^^^^ definition local229
    while (nextItem == null && iterator.hasNext()) {
//         ^^^^^^^^ reference local231
//                             ^^^^^^^^ reference local232
//                                      ^^^^^^^ reference java/util/Iterator#hasNext().
      nextItem = iterator.next();
//    ^^^^^^^^ reference local233
//               ^^^^^^^^ reference local234
//                        ^^^^ reference java/util/Iterator#next().

      if (nextItem.pair == null) {
//        ^^^^^^^^ reference local235
//                 ^^^^ reference pair#
        // Skip this one and go on to the next
        nextItem = null;
//      ^^^^^^^^ reference local236
      }
    }

    return nextItem;
//         ^^^^^^^^ reference local237
  }
}

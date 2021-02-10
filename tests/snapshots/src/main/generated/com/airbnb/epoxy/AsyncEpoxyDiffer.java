package com.airbnb.epoxy;

import android.os.Handler;
//     ^^^^^^^ reference android/
//             ^^ reference android/os/
//                ^^^^^^^ reference android/os/Handler#

import java.util.Collections;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^^ reference java/util/Collections#
import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#
import java.util.concurrent.Executor;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^^ reference java/util/concurrent/
//                          ^^^^^^^^ reference java/util/concurrent/Executor#

import androidx.annotation.AnyThread;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^^ reference androidx/annotation/AnyThread#
import androidx.annotation.NonNull;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^ reference androidx/annotation/NonNull#
import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#
import androidx.recyclerview.widget.DiffUtil;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^ reference androidx/recyclerview/widget/DiffUtil#
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^^^ reference androidx/recyclerview/
//                           ^^^^^^ reference androidx/recyclerview/widget/
//                                  ^^^^^^^^ reference androidx/recyclerview/widget/DiffUtil/
//                                           ^^^^^^^^^^^^ reference androidx/recyclerview/widget/DiffUtil/ItemCallback#

/**
 * An adaptation of Google's {@link androidx.recyclerview.widget.AsyncListDiffer}
 * that adds support for payloads in changes.
 * <p>
 * Also adds support for canceling an in progress diff, and makes everything thread safe.
 */
class AsyncEpoxyDiffer {
^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#

  interface ResultCallback {
  ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#ResultCallback#
    void onResult(@NonNull DiffResult result);
//       ^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#ResultCallback#onResult().
//                 ^^^^^^^ reference androidx/annotation/NonNull#
//                         ^^^^^^^^^^ reference _root_/
//                                    ^^^^^^ definition local0
  }

  private final Executor executor;
//              ^^^^^^^^ reference java/util/concurrent/Executor#
//                       ^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#executor.
  private final ResultCallback resultCallback;
//              ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#ResultCallback#
//                             ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#resultCallback.
  private final ItemCallback<EpoxyModel<?>> diffCallback;
//              ^^^^^^^^^^^^ reference _root_/
//                           ^^^^^^^^^^ reference _root_/
//                                          ^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#diffCallback.
  private final GenerationTracker generationTracker = new GenerationTracker();
//              ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#
//                                ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#generationTracker.
//                                                    ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#`<init>`().
//                                                        ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#

  AsyncEpoxyDiffer(
  ^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#`<init>`().
      @NonNull Handler handler,
//     ^^^^^^^ reference androidx/annotation/NonNull#
//             ^^^^^^^ reference _root_/
//                     ^^^^^^^ definition local2
      @NonNull ResultCallback resultCallback,
//     ^^^^^^^ reference androidx/annotation/NonNull#
//             ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#ResultCallback#
//                            ^^^^^^^^^^^^^^ definition local4
      @NonNull ItemCallback<EpoxyModel<?>> diffCallback
//     ^^^^^^^ reference androidx/annotation/NonNull#
//             ^^^^^^^^^^^^ reference _root_/
//                          ^^^^^^^^^^ reference _root_/
//                                         ^^^^^^^^^^^^ definition local6
  ) {
    this.executor = new HandlerExecutor(handler);
//  ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#this.
//       ^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#executor.
//                      ^^^^^^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^ reference local8
    this.resultCallback = resultCallback;
//  ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#this.
//       ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#resultCallback.
//                        ^^^^^^^^^^^^^^ reference local9
    this.diffCallback = diffCallback;
//  ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#this.
//       ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#diffCallback.
//                      ^^^^^^^^^^^^ reference local10
  }

  @Nullable
   ^^^^^^^^ reference androidx/annotation/Nullable#
  private volatile List<? extends EpoxyModel<?>> list;
//                 ^^^^ reference java/util/List#
//                                ^^^^^^^^^^ reference _root_/
//                                               ^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#list.

  /**
   * Non-null, unmodifiable version of list.
   * <p>
   * Collections.emptyList when list is null, wrapped by Collections.unmodifiableList otherwise
   */
  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  private volatile List<? extends EpoxyModel<?>> readOnlyList = Collections.emptyList();
//                 ^^^^ reference java/util/List#
//                                ^^^^^^^^^^ reference _root_/
//                                               ^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#readOnlyList.
//                                                              ^^^^^^^^^^^ reference java/util/Collections#
//                                                                          ^^^^^^^^^ reference java/util/Collections#emptyList().

  /**
   * Get the current List - any diffing to present this list has already been computed and
   * dispatched via the ListUpdateCallback.
   * <p>
   * If a <code>null</code> List, or no List has been submitted, an empty list will be returned.
   * <p>
   * The returned list may not be mutated - mutations to content must be done through
   * {@link #submitList(List)}.
   *
   * @return current List.
   */
  @AnyThread
   ^^^^^^^^^ reference androidx/annotation/AnyThread#
  @NonNull
   ^^^^^^^ reference androidx/annotation/NonNull#
  public List<? extends EpoxyModel<?>> getCurrentList() {
//       ^^^^ reference java/util/List#
//                      ^^^^^^^^^^ reference _root_/
//                                     ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#getCurrentList().
    return readOnlyList;
//         ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#readOnlyList.
  }

  /**
   * Prevents any ongoing diff from dispatching results. Returns true if there was an ongoing
   * diff to cancel, false otherwise.
   */
  @SuppressWarnings("WeakerAccess")
   ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
  @AnyThread
   ^^^^^^^^^ reference androidx/annotation/AnyThread#
  public boolean cancelDiff() {
//               ^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#cancelDiff().
    return generationTracker.finishMaxGeneration();
//         ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#generationTracker.
//                           ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#finishMaxGeneration().
  }

  /**
   * @return True if a diff operation is in progress.
   */
  @SuppressWarnings("WeakerAccess")
   ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
  @AnyThread
   ^^^^^^^^^ reference androidx/annotation/AnyThread#
  public boolean isDiffInProgress() {
//               ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#isDiffInProgress().
    return generationTracker.hasUnfinishedGeneration();
//         ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#generationTracker.
//                           ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#hasUnfinishedGeneration().
  }

  /**
   * Set the current list without performing any diffing. Cancels any diff in progress.
   * <p>
   * This can be used if you notified a change to the adapter manually and need this list to be
   * synced.
   */
  @AnyThread
   ^^^^^^^^^ reference androidx/annotation/AnyThread#
  public synchronized boolean forceListOverride(@Nullable List<EpoxyModel<?>> newList) {
//                            ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#forceListOverride().
//                                               ^^^^^^^^ reference androidx/annotation/Nullable#
//                                                        ^^^^ reference java/util/List#
//                                                             ^^^^^^^^^^ reference _root_/
//                                                                            ^^^^^^^ definition local11
    // We need to make sure that generation changes and list updates are synchronized
    final boolean interruptedDiff = cancelDiff();
//                ^^^^^^^^^^^^^^^ definition local13
//                                  ^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#cancelDiff().
    int generation = generationTracker.incrementAndGetNextScheduled();
//      ^^^^^^^^^^ definition local15
//                   ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#generationTracker.
//                                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#incrementAndGetNextScheduled().
    tryLatchList(newList, generation);
//  ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#tryLatchList().
//               ^^^^^^^ reference local17
//                        ^^^^^^^^^^ reference local18
    return interruptedDiff;
//         ^^^^^^^^^^^^^^^ reference local19
  }

  /**
   * Set a new List representing your latest data.
   * <p>
   * A diff will be computed between this list and the last list set. If this has not previously
   * been called then an empty list is used as the previous list.
   * <p>
   * The diff computation will be done on the thread given by the handler in the constructor.
   * When the diff is done it will be applied (dispatched to the result callback),
   * and the new List will be swapped in.
   */
  @AnyThread
   ^^^^^^^^^ reference androidx/annotation/AnyThread#
  @SuppressWarnings("WeakerAccess")
   ^^^^^^^^^^^^^^^^ reference java/lang/SuppressWarnings#
  public void submitList(@Nullable final List<? extends EpoxyModel<?>> newList) {
//            ^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#submitList().
//                        ^^^^^^^^ reference androidx/annotation/Nullable#
//                                       ^^^^ reference java/util/List#
//                                                      ^^^^^^^^^^ reference _root_/
//                                                                     ^^^^^^^ definition local20
    final int runGeneration;
//            ^^^^^^^^^^^^^ definition local22
    @Nullable final List<? extends EpoxyModel<?>> previousList;
//   ^^^^^^^^ reference androidx/annotation/Nullable#
//                  ^^^^ reference java/util/List#
//                                 ^^^^^^^^^^ reference _root_/
//                                                ^^^^^^^^^^^^ definition local24

    synchronized (this) {
//                ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#this.
      // Incrementing generation means any currently-running diffs are discarded when they finish
      // We synchronize to guarantee list object and generation number are in sync
      runGeneration = generationTracker.incrementAndGetNextScheduled();
//    ^^^^^^^^^^^^^ reference local26
//                    ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#generationTracker.
//                                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#incrementAndGetNextScheduled().
      previousList = list;
//    ^^^^^^^^^^^^ reference local27
//                   ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#list.
    }

    if (newList == previousList) {
//      ^^^^^^^ reference local28
//                 ^^^^^^^^^^^^ reference local29
      // nothing to do
      onRunCompleted(runGeneration, newList, DiffResult.noOp(previousList));
//    ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#onRunCompleted().
//                   ^^^^^^^^^^^^^ reference local30
//                                  ^^^^^^^ reference local31
//                                           ^^^^^^^^^^ reference _root_/
//                                                      ^^^^ reference noOp#
//                                                           ^^^^^^^^^^^^ reference local32
      return;
    }

    if (newList == null || newList.isEmpty()) {
//      ^^^^^^^ reference local33
//                         ^^^^^^^ reference local34
//                                 ^^^^^^^ reference java/util/List#isEmpty().
      // fast simple clear all
      DiffResult result = null;
//    ^^^^^^^^^^ reference _root_/
//               ^^^^^^ definition local35
      if (previousList != null && !previousList.isEmpty()) {
//        ^^^^^^^^^^^^ reference local37
//                                 ^^^^^^^^^^^^ reference local38
//                                              ^^^^^^^ reference java/util/List#isEmpty().
        result = DiffResult.clear(previousList);
//      ^^^^^^ reference local39
//               ^^^^^^^^^^ reference _root_/
//                          ^^^^^ reference clear#
//                                ^^^^^^^^^^^^ reference local40
      }
      onRunCompleted(runGeneration, null, result);
//    ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#onRunCompleted().
//                   ^^^^^^^^^^^^^ reference local41
//                                        ^^^^^^ reference local42
      return;
    }

    if (previousList == null || previousList.isEmpty()) {
//      ^^^^^^^^^^^^ reference local43
//                              ^^^^^^^^^^^^ reference local44
//                                           ^^^^^^^ reference java/util/List#isEmpty().
      // fast simple first insert
      onRunCompleted(runGeneration, newList, DiffResult.inserted(newList));
//    ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#onRunCompleted().
//                   ^^^^^^^^^^^^^ reference local45
//                                  ^^^^^^^ reference local46
//                                           ^^^^^^^^^^ reference _root_/
//                                                      ^^^^^^^^ reference inserted#
//                                                               ^^^^^^^ reference local47
      return;
    }

    final DiffCallback wrappedCallback = new DiffCallback(previousList, newList, diffCallback);
//        ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#
//                     ^^^^^^^^^^^^^^^ definition local48
//                                       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#`<init>`().
//                                           ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#
//                                                        ^^^^^^^^^^^^ reference local50
//                                                                      ^^^^^^^ reference local51
//                                                                               ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#diffCallback.

    executor.execute(new Runnable() {
//  ^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#executor.
//           ^^^^^^^ reference java/util/concurrent/Executor#execute().
//                   ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#submitList().``#`<init>`(). 6:5
//                       ^^^^^^^^ reference java/lang/Runnable#
//                       ^^^^^^^^ reference java/lang/Runnable#
//                                  ^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#submitList().``#`<init>`(). 1:4
      @Override
//     ^^^^^^^^ reference java/lang/Override#
      public void run() {
//                ^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#submitList().``#run().
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(wrappedCallback);
//      ^^^^^^^^ reference DiffUtil/
//               ^^^^^^^^^^ reference DiffUtil/DiffResult#
//                          ^^^^^^ definition local52
//                                   ^^^^^^^^ reference _root_/
//                                            ^^^^^^^^^^^^^ reference calculateDiff#
//                                                          ^^^^^^^^^^^^^^^ reference local54
        onRunCompleted(runGeneration, newList, DiffResult.diff(previousList, newList, result));
//      ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#onRunCompleted().
//                     ^^^^^^^^^^^^^ reference local55
//                                    ^^^^^^^ reference local56
//                                             ^^^^^^^^^^ reference _root_/
//                                                        ^^^^ reference diff#
//                                                             ^^^^^^^^^^^^ reference local57
//                                                                           ^^^^^^^ reference local58
//                                                                                    ^^^^^^ reference local59
      }
    });
  }

  private void onRunCompleted(
//             ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#onRunCompleted().
      final int runGeneration,
//              ^^^^^^^^^^^^^ definition local60
      @Nullable final List<? extends EpoxyModel<?>> newList,
//     ^^^^^^^^ reference androidx/annotation/Nullable#
//                    ^^^^ reference java/util/List#
//                                   ^^^^^^^^^^ reference _root_/
//                                                  ^^^^^^^ definition local62
      @Nullable final DiffResult result
//     ^^^^^^^^ reference androidx/annotation/Nullable#
//                    ^^^^^^^^^^ reference _root_/
//                               ^^^^^^ definition local64
  ) {

    // We use an asynchronous handler so that the Runnable can be posted directly back to the main
    // thread without waiting on view invalidation synchronization.
    MainThreadExecutor.ASYNC_INSTANCE.execute(new Runnable() {
//  ^^^^^^^^^^^^^^^^^^ reference MainThreadExecutor/
//                     ^^^^^^^^^^^^^^ reference MainThreadExecutor/ASYNC_INSTANCE#
//                                    ^^^^^^^ reference MainThreadExecutor/ASYNC_INSTANCE#execute#
//                                            ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#onRunCompleted().``#`<init>`(). 8:5
//                                                ^^^^^^^^ reference java/lang/Runnable#
//                                                ^^^^^^^^ reference java/lang/Runnable#
//                                                           ^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#onRunCompleted().``#`<init>`(). 1:4
      @Override
//     ^^^^^^^^ reference java/lang/Override#
      public void run() {
//                ^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#onRunCompleted().``#run().
        final boolean dispatchResult = tryLatchList(newList, runGeneration);
//                    ^^^^^^^^^^^^^^ definition local66
//                                     ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#tryLatchList().
//                                                  ^^^^^^^ reference local68
//                                                           ^^^^^^^^^^^^^ reference local69
        if (result != null && dispatchResult) {
//          ^^^^^^ reference local70
//                            ^^^^^^^^^^^^^^ reference local71
          resultCallback.onResult(result);
//        ^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#resultCallback.
//                       ^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#ResultCallback#onResult().
//                                ^^^^^^ reference local72
        }
      }
    });
  }

  /**
   * Marks the generation as done, and updates the list if the generation is the most recent.
   *
   * @return True if the given generation is the most recent, in which case the given list was
   * set. False if the generation is old and the list was ignored.
   */
  @AnyThread
   ^^^^^^^^^ reference androidx/annotation/AnyThread#
  private synchronized boolean tryLatchList(@Nullable List<? extends EpoxyModel<?>> newList,
//                             ^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#tryLatchList().
//                                           ^^^^^^^^ reference androidx/annotation/Nullable#
//                                                    ^^^^ reference java/util/List#
//                                                                   ^^^^^^^^^^ reference _root_/
//                                                                                  ^^^^^^^ definition local73
      int runGeneration) {
//        ^^^^^^^^^^^^^ definition local75
    if (generationTracker.finishGeneration(runGeneration)) {
//      ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#generationTracker.
//                        ^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#finishGeneration().
//                                         ^^^^^^^^^^^^^ reference local77
      list = newList;
//    ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#list.
//           ^^^^^^^ reference local78

      if (newList == null) {
//        ^^^^^^^ reference local79
        readOnlyList = Collections.emptyList();
//      ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#readOnlyList.
//                     ^^^^^^^^^^^ reference java/util/Collections#
//                                 ^^^^^^^^^ reference java/util/Collections#emptyList().
      } else {
        readOnlyList = Collections.unmodifiableList(newList);
//      ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#readOnlyList.
//                     ^^^^^^^^^^^ reference java/util/Collections#
//                                 ^^^^^^^^^^^^^^^^ reference java/util/Collections#unmodifiableList().
//                                                  ^^^^^^^ reference local80
      }

      return true;
    }

    return false;
  }

  /**
   * The concept of a "generation" is used to associate a diff result with a point in time when
   * it was created. This allows us to handle list updates concurrently, and ignore outdated diffs.
   * <p>
   * We track the highest start generation, and the highest finished generation, and these must
   * be kept in sync, so all access to this class is synchronized.
   * <p>
   * The general synchronization strategy for this class is that when a generation number
   * is queried that action must be synchronized with accessing the current list, so that the
   * generation number is synced with the list state at the time it was created.
   */
  private static class GenerationTracker {
//               ^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#`<init>`().
//               ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#

    // Max generation of currently scheduled runnable
    private volatile int maxScheduledGeneration;
//                       ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxScheduledGeneration.
    private volatile int maxFinishedGeneration;
//                       ^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxFinishedGeneration.

    synchronized int incrementAndGetNextScheduled() {
//                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#incrementAndGetNextScheduled().
      return ++maxScheduledGeneration;
//             ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxScheduledGeneration.
    }

    synchronized boolean finishMaxGeneration() {
//                       ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#finishMaxGeneration().
      boolean isInterrupting = hasUnfinishedGeneration();
//            ^^^^^^^^^^^^^^ definition local81
//                             ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#hasUnfinishedGeneration().
      maxFinishedGeneration = maxScheduledGeneration;
//    ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxFinishedGeneration.
//                            ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxScheduledGeneration.
      return isInterrupting;
//           ^^^^^^^^^^^^^^ reference local83
    }

    synchronized boolean hasUnfinishedGeneration() {
//                       ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#hasUnfinishedGeneration().
      return maxScheduledGeneration > maxFinishedGeneration;
//           ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxScheduledGeneration.
//                                    ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxFinishedGeneration.
    }

    synchronized boolean finishGeneration(int runGeneration) {
//                       ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#finishGeneration().
//                                            ^^^^^^^^^^^^^ definition local84
      boolean isLatestGeneration =
//            ^^^^^^^^^^^^^^^^^^ definition local86
          maxScheduledGeneration == runGeneration && runGeneration > maxFinishedGeneration;
//        ^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxScheduledGeneration.
//                                  ^^^^^^^^^^^^^ reference local88
//                                                   ^^^^^^^^^^^^^ reference local89
//                                                                   ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxFinishedGeneration.

      if (isLatestGeneration) {
//        ^^^^^^^^^^^^^^^^^^ reference local90
        maxFinishedGeneration = runGeneration;
//      ^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#GenerationTracker#maxFinishedGeneration.
//                              ^^^^^^^^^^^^^ reference local91
      }

      return isLatestGeneration;
//           ^^^^^^^^^^^^^^^^^^ reference local92
    }
  }

  private static class DiffCallback extends DiffUtil.Callback {
//               ^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#
//                                          ^^^^^^^^ reference DiffUtil/
//                                                   ^^^^^^^^ reference DiffUtil/Callback#

    final List<? extends EpoxyModel<?>> oldList;
//        ^^^^ reference java/util/List#
//                       ^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#oldList.
    final List<? extends EpoxyModel<?>> newList;
//        ^^^^ reference java/util/List#
//                       ^^^^^^^^^^ reference _root_/
//                                      ^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#newList.
    private final ItemCallback<EpoxyModel<?>> diffCallback;
//                ^^^^^^^^^^^^ reference _root_/
//                             ^^^^^^^^^^ reference _root_/
//                                            ^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#diffCallback.

    DiffCallback(List<? extends EpoxyModel<?>> oldList, List<? extends EpoxyModel<?>> newList,
//  ^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#`<init>`().
//               ^^^^ reference java/util/List#
//                              ^^^^^^^^^^ reference _root_/
//                                             ^^^^^^^ definition local93
//                                                      ^^^^ reference java/util/List#
//                                                                     ^^^^^^^^^^ reference _root_/
//                                                                                    ^^^^^^^ definition local95
        ItemCallback<EpoxyModel<?>> diffCallback) {
//      ^^^^^^^^^^^^ reference _root_/
//                   ^^^^^^^^^^ reference _root_/
//                                  ^^^^^^^^^^^^ definition local97
      this.oldList = oldList;
//    ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#this.
//         ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#oldList.
//                   ^^^^^^^ reference local99
      this.newList = newList;
//    ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#this.
//         ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#newList.
//                   ^^^^^^^ reference local100
      this.diffCallback = diffCallback;
//    ^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#this.
//         ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#diffCallback.
//                        ^^^^^^^^^^^^ reference local101
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public int getOldListSize() {
//             ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#getOldListSize().
      return oldList.size();
//           ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#oldList.
//                   ^^^^ reference java/util/List#size().
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public int getNewListSize() {
//             ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#getNewListSize().
      return newList.size();
//           ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#newList.
//                   ^^^^ reference java/util/List#size().
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                 ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#areItemsTheSame().
//                                     ^^^^^^^^^^^^^^^ definition local102
//                                                          ^^^^^^^^^^^^^^^ definition local104
      return diffCallback.areItemsTheSame(
//           ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#diffCallback.
//                        ^^^^^^^^^^^^^^^ reference `<any>`#areItemsTheSame#
          oldList.get(oldItemPosition),
//        ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#oldList.
//                ^^^ reference java/util/List#get().
//                    ^^^^^^^^^^^^^^^ reference local106
          newList.get(newItemPosition)
//        ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#newList.
//                ^^^ reference java/util/List#get().
//                    ^^^^^^^^^^^^^^^ reference local107
      );
    }

    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                 ^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#areContentsTheSame().
//                                        ^^^^^^^^^^^^^^^ definition local108
//                                                             ^^^^^^^^^^^^^^^ definition local110
      return diffCallback.areContentsTheSame(
//           ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#diffCallback.
//                        ^^^^^^^^^^^^^^^^^^ reference `<any>`#areContentsTheSame#
          oldList.get(oldItemPosition),
//        ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#oldList.
//                ^^^ reference java/util/List#get().
//                    ^^^^^^^^^^^^^^^ reference local112
          newList.get(newItemPosition)
//        ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#newList.
//                ^^^ reference java/util/List#get().
//                    ^^^^^^^^^^^^^^^ reference local113
      );
    }

    @Nullable
//   ^^^^^^^^ reference androidx/annotation/Nullable#
    @Override
//   ^^^^^^^^ reference java/lang/Override#
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
//         ^^^^^^ reference java/lang/Object#
//                ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#getChangePayload().
//                                     ^^^^^^^^^^^^^^^ definition local114
//                                                          ^^^^^^^^^^^^^^^ definition local116
      return diffCallback.getChangePayload(
//           ^^^^^^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#diffCallback.
//                        ^^^^^^^^^^^^^^^^ reference `<any>`#getChangePayload#
          oldList.get(oldItemPosition),
//        ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#oldList.
//                ^^^ reference java/util/List#get().
//                    ^^^^^^^^^^^^^^^ reference local118
          newList.get(newItemPosition)
//        ^^^^^^^ reference com/airbnb/epoxy/AsyncEpoxyDiffer#DiffCallback#newList.
//                ^^^ reference java/util/List#get().
//                    ^^^^^^^^^^^^^^^ reference local119
      );
    }
  }
}

package com.dfsek.terra.api.task;

public interface TaskScheduler {
    /**
     * Run a task synchronously on the next tick.
     * Functionally equivalent to {@link #runTask(Runnable, long)}
     * @param task Task to run.
     */
    default void runTask(Runnable task) {
        runTask(task, 0);
    }

    /**
     * Schedule a task asynchronously immediately.
     * Functionally equivalent to {@link #runTaskAsynchronously(Runnable)} (Runnable, long)}
     * @param task Task to run.
     */
    default void runTaskAsynchronously(Runnable task) {
        runTaskAsynchronously(task, 0);
    }

    /**
     * Run a task asynchronously after a number of ticks.
     * @param task Task to run.
     * @param ticks Delay before running the task, in ticks.
     */
    void runTaskAsynchronously(Runnable task, long ticks);

    /**
     * Run a task synchronously after a number of ticks.
     * @param task Task to run.
     * @param ticks Delay before running the task, in ticks.
     */
    void runTask(Runnable task, long ticks);
}

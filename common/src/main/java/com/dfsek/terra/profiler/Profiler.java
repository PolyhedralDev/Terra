package com.dfsek.terra.profiler;

import java.util.Map;

public interface Profiler {
    /**
     * Push a frame to this profiler.
     *
     * @param frame ID of frame.
     */
    void push(String frame);

    /**
     * Pop a frame from this profiler.
     *
     * @param frame ID of frame. Must match ID
     *              at the top of the profiler stack.
     */
    void pop(String frame);

    /**
     * Start profiling.
     */
    void start();

    /**
     * Stop profiling.
     */
    void stop();

    /**
     * Get the profiler data.
     *
     * @return Profiler data.
     */
    Map<String, Timings> getTimings();

    /**
     * Return a {@link AutoCloseable} implementation that
     * may be used in a try-with-resources statement for
     * more intuitive profiling, with auto-push/pop.
     *
     * @param frame ID of frame.
     * @return {@link AutoCloseable} implementation for use
     * in try-with-resources.
     */
    default ProfileFrame profile(String frame) {
        push(frame);
        return new ProfileFrame(() -> pop(frame));
    }

    /**
     * Clear the profiler data.
     */
    void reset();
}

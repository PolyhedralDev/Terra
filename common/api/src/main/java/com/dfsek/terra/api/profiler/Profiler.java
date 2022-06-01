/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.profiler;

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
     * Clear the profiler data.
     */
    void reset();
    
    /**
     * Get the profiler data.
     *
     * @return Profiler data.
     */
    Map<String, Timings> getTimings();
}

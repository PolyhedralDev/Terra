package com.dfsek.terra.profiler;

import java.util.Map;

public interface Profiler {
    void push(String frame);

    void pop(String frame);

    void start();

    void stop();

    Map<String, Timings> getTimings();

    default AutoCloseable profile(String frame) {
        push(frame);
        return () -> pop(frame);
    }
}

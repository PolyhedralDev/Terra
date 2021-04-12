package com.dfsek.terra.profiler;

import java.util.Map;

public interface Profiler {
    ProfilerImpl INSTANCE = new ProfilerImpl();

    static Profiler getInstance() {
        return INSTANCE;
    }

    void push(String frame);

    void pop(String frame);

    void start();

    void stop();

    Map<String, Timings> getTimings();
}

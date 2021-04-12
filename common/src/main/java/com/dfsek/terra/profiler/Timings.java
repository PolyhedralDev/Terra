package com.dfsek.terra.profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timings {
    private final Map<String, Timings> subItems = new HashMap<>();

    private final List<Long> timings = new ArrayList<>();

    public void addTime(long time) {
        timings.add(time);
    }

    public List<Long> getTimings() {
        return timings;
    }

    public double average() {
        return (double) timings.stream().reduce(0L, Long::sum) / timings.size();
    }

    public long max() {
        return timings.stream().mapToLong(Long::longValue).max().orElse(0L);
    }

    public long min() {
        return timings.stream().mapToLong(Long::longValue).min().orElse(0L);
    }

    public Timings getSubItem(String id) {
        return subItems.computeIfAbsent(id, s -> new Timings());
    }

    public String toString(int indent) {
        StringBuilder builder = new StringBuilder();

        builder.append("Avg ").append(average() / 1000000).append("ms");

        subItems.forEach((id, timings) -> {
            builder.append('\n');
            for(int i = 0; i <= indent; i++) {
                builder.append('\t');
            }
            builder.append(id).append(": ").append(timings.toString(indent + 1));
        });
        return builder.toString();
    }

    @Override
    public String toString() {
        return toString(0);
    }
}

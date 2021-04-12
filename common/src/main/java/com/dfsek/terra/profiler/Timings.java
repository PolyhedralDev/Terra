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

    public double sum() {
        return timings.stream().mapToDouble(Long::doubleValue).sum();
    }

    public Timings getSubItem(String id) {
        return subItems.computeIfAbsent(id, s -> new Timings());
    }

    public String toString(int indent, Timings parent) {
        StringBuilder builder = new StringBuilder();

        builder.append((double) min() / 1000000).append("ms min / ").append(average() / 1000000).append("ms avg / ")
                .append((double) max() / 1000000).append("ms max (").append(timings.size()).append(" samples, ")
                .append((sum() / parent.sum()) * 100).append("% of parent)");

        subItems.forEach((id, timings) -> {
            builder.append('\n');
            for(int i = 0; i <= indent; i++) {
                builder.append('\t');
            }
            builder.append(id).append(": ").append(timings.toString(indent + 1, this));
        });
        return builder.toString();
    }

    @Override
    public String toString() {
        return toString(0, this);
    }
}

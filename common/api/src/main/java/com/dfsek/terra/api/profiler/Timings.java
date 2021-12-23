/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.profiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Timings {
    private final Map<String, Timings> subItems = new HashMap<>();
    
    private final List<Long> timings = new ArrayList<>();
    
    public void addTime(long time) {
        timings.add(time);
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
    
    @Override
    public String toString() {
        return toString(1, this, Collections.emptySet());
    }
    
    private String toString(int indent, Timings parent, Set<Integer> branches) {
        StringBuilder builder = new StringBuilder();
        
        builder.append((double) min() / 1000000).append("ms min / ").append(average() / 1000000).append("ms avg / ")
               .append((double) max() / 1000000).append("ms max (").append(timings.size()).append(" samples, ")
               .append((sum() / parent.sum()) * 100).append("% of parent)");
        
        List<String> frames = new ArrayList<>();
        Set<Integer> newBranches = new HashSet<>(branches);
        newBranches.add(indent);
        subItems.forEach((id, timings) -> frames.add(id + ": " + timings.toString(indent + 1, this, newBranches)));
        
        for(int i = 0; i < frames.size(); i++) {
            builder.append('\n');
            for(int j = 0; j < indent; j++) {
                if(branches.contains(j)) builder.append("│   ");
                else builder.append("    ");
            }
            if(i == frames.size() - 1 && !frames.get(i).contains("\n")) builder.append("└───");
            else builder.append("├───");
            builder.append(frames.get(i));
        }
        return builder.toString();
    }
    
    public List<Long> getTimings() {
        return timings;
    }
    
    public Timings getSubItem(String id) {
        return subItems.computeIfAbsent(id, s -> new Timings());
    }
}

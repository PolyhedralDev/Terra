package com.dfsek.terra.profiler;

public class Frame {
    private final String id;
    private final long start;

    public Frame(String id) {
        this.id = id;
        this.start = System.nanoTime();
    }

    public String getId() {
        return id;
    }

    public long getStart() {
        return start;
    }

    @Override
    public String toString() {
        return id;
    }
}

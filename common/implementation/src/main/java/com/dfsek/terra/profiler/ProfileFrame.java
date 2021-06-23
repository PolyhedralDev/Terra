package com.dfsek.terra.profiler;

public class ProfileFrame implements AutoCloseable {
    private final Runnable action;

    public ProfileFrame(Runnable action) {
        this.action = action;
    }

    @Override
    public void close() {
        action.run();
    }
}

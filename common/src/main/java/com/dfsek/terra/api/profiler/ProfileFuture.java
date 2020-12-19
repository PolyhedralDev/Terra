package com.dfsek.terra.api.profiler;

import java.util.concurrent.CompletableFuture;

public class ProfileFuture extends CompletableFuture<Boolean> implements AutoCloseable {
    public ProfileFuture() {
        super();
    }

    public boolean complete() {
        return super.complete(true);
    }

    @Override
    public void close() {
        this.complete();
    }
}

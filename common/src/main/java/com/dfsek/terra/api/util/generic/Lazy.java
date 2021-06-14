package com.dfsek.terra.api.util.generic;

import java.util.function.Supplier;

public final class Lazy<T> {
    private final Supplier<T> fetch;

    private T value;

    private boolean needsInit = true;

    public Lazy(Supplier<T> fetch) {
        this.fetch = fetch;
    }

    public T get() {
        if(needsInit) {
            value = fetch.get();
            needsInit = false;
        }
        return value;
    }
}

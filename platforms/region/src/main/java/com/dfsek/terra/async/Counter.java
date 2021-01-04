package com.dfsek.terra.async;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Counter {
    private final AtomicInteger integer = new AtomicInteger();
    private final Consumer<Integer> update;

    public Counter(Consumer<Integer> update) {
        this.update = update;
    }

    public void add() {
        update.accept(integer.addAndGet(1));
    }
}

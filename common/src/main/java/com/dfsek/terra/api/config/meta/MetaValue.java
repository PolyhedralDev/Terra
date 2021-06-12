package com.dfsek.terra.api.config.meta;

import java.util.function.Supplier;

public interface MetaValue<T> extends Supplier<T> {
    static <T1> MetaValue<T1> of(T1 value) {
        return () -> value;
    }
}

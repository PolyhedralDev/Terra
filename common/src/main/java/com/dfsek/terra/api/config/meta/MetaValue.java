package com.dfsek.terra.api.config.meta;

public interface MetaValue<T> {
    T load(MetaContext context);
}

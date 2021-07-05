package com.dfsek.terra.api.util;

import com.dfsek.tectonic.exception.ConfigException;

@FunctionalInterface
public interface ExceptionalConsumer<T> {
    void accept(T t) throws ConfigException;
}

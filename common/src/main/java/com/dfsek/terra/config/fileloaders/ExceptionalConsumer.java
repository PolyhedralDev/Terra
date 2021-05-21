package com.dfsek.terra.config.fileloaders;

import com.dfsek.tectonic.exception.ConfigException;

@FunctionalInterface
public interface ExceptionalConsumer<T> {
    void accept(T t) throws ConfigException;
}

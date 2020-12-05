package com.dfsek.terra.config.files;

import com.dfsek.tectonic.exception.ConfigException;

@FunctionalInterface
public interface ExceptionalConsumer<T> {
    void accept(T t) throws ConfigException;
}

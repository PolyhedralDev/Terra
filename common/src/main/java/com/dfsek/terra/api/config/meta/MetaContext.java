package com.dfsek.terra.api.config.meta;

import com.dfsek.tectonic.exception.LoadException;

import java.lang.reflect.Type;

/**
 * Context from which to pull {@link MetaValue}s
 */
public interface MetaContext {
    default <T> T load(String meta, Class<T> clazz) throws LoadException {
        return load(meta, (Type) clazz);
    }

    <T> T load(String meta, Type type) throws LoadException;
}

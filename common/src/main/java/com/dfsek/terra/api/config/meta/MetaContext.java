package com.dfsek.terra.api.config.meta;

import com.dfsek.tectonic.exception.ConfigException;

/**
 * Context from which to pull {@link MetaValue}s
 */
public interface MetaContext {
    <T> T load(String meta, Class<T> clazz) throws ConfigException;
}

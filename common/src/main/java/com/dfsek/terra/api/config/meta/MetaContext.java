package com.dfsek.terra.api.config.meta;

/**
 * Context from which to pull {@link MetaValue}s
 */
public interface MetaContext {
    <T> T load(Object in, Class<T> clazz);

    /**
     * Load method for when class is unknown.
     *
     * @param in Object to load/load metavalue for
     * @return Loaded object
     */
    default Object load(Object in) {
        return load(in, in.getClass());
    }
}

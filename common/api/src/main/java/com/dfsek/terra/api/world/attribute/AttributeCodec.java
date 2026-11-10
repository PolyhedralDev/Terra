package com.dfsek.terra.api.world.attribute;

public interface AttributeCodec<T> {
    /**
     * @param raw a number, string, boolean, list, string-keyed map, or {@code null}
     */
    T decode(Object raw, DecodeContext context) throws DecodeException;

    /**
     * Returns a number, boolean, string, list or string-keyed map of those, or {@code null}. {@link #decode} must read it
     * back to an equal value.
     */
    Object encode(T value);
}

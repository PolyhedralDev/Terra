package com.dfsek.terra.api.world.attribute;

import java.lang.reflect.Type;


public interface DecodeContext {
    /**
     * {@code type} is {@code Float}, {@code Integer}, {@code Double}, {@code Boolean}, {@code String}, {@code List<Object>},
     * {@code Map<String, Object>} or {@code Object}.
     */
    <V> V load(Type type, Object raw) throws DecodeException;

    /**
     * Call before checking a value's shape. Numeric expressions stay strings; {@link #load} evaluates them.
     */
    Object resolve(Object raw) throws DecodeException;
}

package com.dfsek.terra.api.world.attribute;

@FunctionalInterface
public interface AttributeLookup {
    /**
     * @throws DecodeException if {@code key} names no usable attribute; the message is reported as the reason
     */
    Attribute<?> find(String key) throws DecodeException;
}

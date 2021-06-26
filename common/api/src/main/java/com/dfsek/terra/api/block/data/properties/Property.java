package com.dfsek.terra.api.block.data.properties;

import java.util.Collection;

public interface Property<T> {
    Class<T> getType();

    String getName();

    Collection<T> values();
}

package com.dfsek.terra.api.block.state.properties;

import java.util.Collection;

public interface Property<T> {
    Class<T> getType();

    String getName();

    Collection<T> values();
}

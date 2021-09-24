package com.dfsek.terra.api.block.state.properties;

import java.util.Collection;


public interface Property<T> {
    Collection<T> values();
    
    Class<T> getType();
    
    String getName();
}

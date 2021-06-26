package com.dfsek.terra.api.block.data.properties.base;

import com.dfsek.terra.api.block.data.properties.Property;

public interface IntProperty extends Property<Integer> {
    @Override
    default Class<Integer> getType() {
        return Integer.class;
    }
}

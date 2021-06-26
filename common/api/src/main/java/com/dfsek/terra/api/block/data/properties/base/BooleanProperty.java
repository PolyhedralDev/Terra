package com.dfsek.terra.api.block.data.properties.base;

import com.dfsek.terra.api.block.data.properties.Property;

public interface BooleanProperty extends Property<Boolean> {
    @Override
    default Class<Boolean> getType() {
        return Boolean.class;
    }
}

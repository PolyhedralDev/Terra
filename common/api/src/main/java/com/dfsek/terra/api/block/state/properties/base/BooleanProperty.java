package com.dfsek.terra.api.block.state.properties.base;

import com.dfsek.terra.api.block.state.properties.Property;

import java.util.Arrays;
import java.util.Collection;

public interface BooleanProperty extends Property<Boolean> {
    static BooleanProperty of(String name) {
        return new BooleanProperty() {
            private static final Collection<Boolean> BOOLEANS = Arrays.asList(true, false);

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Collection<Boolean> values() {
                return BOOLEANS;
            }
        };
    }

    @Override
    default Class<Boolean> getType() {
        return Boolean.class;
    }
}

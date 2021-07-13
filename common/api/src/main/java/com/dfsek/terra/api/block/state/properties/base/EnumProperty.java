package com.dfsek.terra.api.block.state.properties.base;

import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.api.util.generic.Lazy;

import java.util.Arrays;
import java.util.Collection;

public interface EnumProperty<T extends Enum<T>> extends Property<T> {
    static <T extends Enum<T>> EnumProperty<T> of(String name, Class<T> clazz) {
        return new EnumProperty<T>() {
            private final Lazy<Collection<T>> constants = Lazy.of(() -> Arrays.asList(clazz.getEnumConstants()));

            @Override
            public Class<T> getType() {
                return clazz;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Collection<T> values() {
                return constants.value();
            }
        };
    }
}

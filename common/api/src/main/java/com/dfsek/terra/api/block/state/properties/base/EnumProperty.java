/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.state.properties.base;

import java.util.Arrays;
import java.util.Collection;

import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.api.util.generic.Lazy;


public interface EnumProperty<T extends Enum<T>> extends Property<T> {
    static <T extends Enum<T>> EnumProperty<T> of(String name, Class<T> clazz) {
        return new EnumProperty<>() {
            private final Lazy<Collection<T>> constants = Lazy.lazy(() -> Arrays.asList(clazz.getEnumConstants()));
            
            @Override
            public Collection<T> values() {
                return constants.value();
            }
            
            @Override
            public Class<T> getType() {
                return clazz;
            }
            
            @Override
            public String getID() {
                return name;
            }
        };
    }
}

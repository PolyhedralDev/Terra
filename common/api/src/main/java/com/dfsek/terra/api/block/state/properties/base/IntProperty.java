/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.state.properties.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.api.util.generic.Construct;


public interface IntProperty extends Property<Integer> {
    static IntProperty of(String name, int min, int max) {
        return new IntProperty() {
            private final Collection<Integer> collection = Construct.construct(() -> {
                List<Integer> ints = new ArrayList<>();
                for(int i = min; i <= max; i++) {
                    ints.add(i);
                }
                return ints;
            });
            
            @Override
            public Collection<Integer> values() {
                return collection;
            }
            
            @Override
            public String getID() {
                return name;
            }
        };
    }
    
    @Override
    default Class<Integer> getType() {
        return Integer.class;
    }
}

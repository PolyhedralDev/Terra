package com.dfsek.terra.api.block.state.properties.base;

import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.api.util.generic.Construct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface IntProperty extends Property<Integer> {
    @Override
    default Class<Integer> getType() {
        return Integer.class;
    }

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
            public String getName() {
                return name;
            }

            @Override
            public Collection<Integer> values() {
                return collection;
            }
        };
    }
}

package com.dfsek.terra.world.attribute;

import org.jetbrains.annotations.Nullable;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;


final class AttributeLayers {
    private AttributeLayers() { }

    @SafeVarargs
    static <T> @Nullable T resolve(Attribute<T> attribute, AttributeEntry<T>... layersBottomToTop) {
        T value = attribute.defaultValue();
        boolean applied = false;
        for(AttributeEntry<T> entry : layersBottomToTop) {
            if(entry == null) continue;
            if(value == null) {
                if(!entry.isOverride()) continue;
                value = entry.apply(attribute.type(), null);
            } else {
                value = entry.apply(attribute.type(), value);
            }
            applied = true;
        }
        if(value == null || !applied) return value;
        return attribute.sanitize(value);
    }

    /**
     * Unrolled to allocate nothing.
     */
    static <T> @Nullable T resolve(Attribute<T> attribute, @Nullable AttributeEntry<T> first, @Nullable AttributeEntry<T> second,
                                   @Nullable AttributeEntry<T> third, @Nullable AttributeEntry<T> fourth) {
        T value = attribute.defaultValue();
        boolean applied = false;
        if(applies(value, first)) {
            value = first.apply(attribute.type(), value);
            applied = true;
        }
        if(applies(value, second)) {
            value = second.apply(attribute.type(), value);
            applied = true;
        }
        if(applies(value, third)) {
            value = third.apply(attribute.type(), value);
            applied = true;
        }
        if(applies(value, fourth)) {
            value = fourth.apply(attribute.type(), value);
            applied = true;
        }
        if(value == null || !applied) return value;
        return attribute.sanitize(value);
    }

    private static <T> boolean applies(@Nullable T value, @Nullable AttributeEntry<T> entry) {
        return entry != null && (value != null || entry.isOverride());
    }
}

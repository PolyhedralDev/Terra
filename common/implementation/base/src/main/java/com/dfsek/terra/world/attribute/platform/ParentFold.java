package com.dfsek.terra.world.attribute.platform;

import java.util.Objects;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;


public final class ParentFold {
    private ParentFold() {
    }

    /**
     * Composes {@code upper} over {@code lower} per attribute, or overrides with their resolved value when they don't compose.
     */
    public static AttributeSlots fold(AttributeHandle handle, Iterable<? extends Attribute<?>> attributes, AttributeSlots lower,
                                      AttributeSlots upper) {
        Objects.requireNonNull(lower, "lower");
        Objects.requireNonNull(upper, "upper");
        AttributeEntry<?>[] slots = new AttributeEntry<?>[handle.attributeCount()];
        for(Attribute<?> attribute : attributes) fold(handle, attribute, lower, upper, slots);
        return AttributeSlots.of(slots);
    }

    private static <T> void fold(AttributeHandle handle, Attribute<T> attribute, AttributeSlots lower, AttributeSlots upper,
                                 AttributeEntry<?>[] slots) {
        AttributeEntry<T> below = lower.get(attribute);
        AttributeEntry<T> above = upper.get(attribute);
        if(below == null && above == null) return;
        slots[attribute.index()] = handle.compose(attribute, below, above)
            .orElseGet(() -> AttributeEntry.override(handle.resolve(attribute, below, above, null)));
    }
}

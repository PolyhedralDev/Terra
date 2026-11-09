package com.dfsek.terra.world.attribute.platform;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;


/**
 * An attribute built after the slots were sized reads as absent.
 */
public final class AttributeSlots {
    public static final AttributeSlots EMPTY = new AttributeSlots(new AttributeEntry<?>[0]);

    private final AttributeEntry<?>[] entries;

    private AttributeSlots(AttributeEntry<?>[] entries) {
        this.entries = entries;
    }

    /**
     * Keeps {@code entries} without copying: the caller must not change the array afterwards.
     */
    public static AttributeSlots of(AttributeEntry<?>[] entries) {
        return new AttributeSlots(Objects.requireNonNull(entries, "entries"));
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable AttributeEntry<T> get(Attribute<T> attribute) {
        int index = attribute.index();
        AttributeEntry<?>[] entries = this.entries;
        return index < entries.length ? (AttributeEntry<T>) entries[index] : null;
    }
}

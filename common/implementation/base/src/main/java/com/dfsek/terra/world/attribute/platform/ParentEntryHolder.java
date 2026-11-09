package com.dfsek.terra.world.attribute.platform;

public interface ParentEntryHolder {
    /**
     * {@link AttributeSlots#EMPTY} until the platform sets them.
     */
    AttributeSlots parentEntries();
}

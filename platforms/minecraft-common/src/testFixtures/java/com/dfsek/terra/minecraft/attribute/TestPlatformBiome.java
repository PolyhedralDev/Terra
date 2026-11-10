package com.dfsek.terra.minecraft.attribute;

import net.minecraft.resources.Identifier;

import java.util.Objects;

import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;
import com.dfsek.terra.world.attribute.platform.ParentEntryHolder;


public final class TestPlatformBiome implements PlatformBiome, ParentEntryHolder {
    private final Identifier parent;
    private AttributeSlots parentEntries = AttributeSlots.EMPTY;

    public TestPlatformBiome(Identifier parent) {
        this.parent = Objects.requireNonNull(parent);
    }

    public Identifier parent() {
        return parent;
    }

    @Override
    public Identifier getHandle() {
        return parent;
    }

    @Override
    public AttributeSlots parentEntries() {
        return parentEntries;
    }

    public void setParentEntries(AttributeSlots parentEntries) {
        this.parentEntries = Objects.requireNonNull(parentEntries);
    }
}

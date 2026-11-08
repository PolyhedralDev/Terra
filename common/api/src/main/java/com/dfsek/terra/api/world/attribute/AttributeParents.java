package com.dfsek.terra.api.world.attribute;

import org.jetbrains.annotations.Nullable;

import com.dfsek.terra.api.world.biome.Biome;


/**
 * Called on read paths: must not allocate.
 */
public interface AttributeParents {
    AttributeParents NONE = new AttributeParents() {
        @Override
        public <T> @Nullable AttributeEntry<T> biomeOrNull(Biome biome, Attribute<T> attribute) {
            return null;
        }

        @Override
        public <T> @Nullable AttributeEntry<T> packOrNull(PackAttributes pack, Attribute<T> attribute) {
            return null;
        }
    };

    /**
     * Sits between the pack entry and the biome's own entry.
     */
    <T> @Nullable AttributeEntry<T> biomeOrNull(Biome biome, Attribute<T> attribute);

    /**
     * Sits between the attribute's default and the pack entry.
     */
    <T> @Nullable AttributeEntry<T> packOrNull(PackAttributes pack, Attribute<T> attribute);
}

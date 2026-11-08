/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.biome;


import org.jetbrains.annotations.NotNull;

import java.util.Set;

import com.dfsek.terra.api.properties.PropertyHolder;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;


/**
 * Represents a Terra biome
 */
public interface Biome extends PropertyHolder, StringIdentifiable {
    /**
     * Gets the platform biome this custom biome delegates to.
     *
     * @return The platform biome.
     */
    PlatformBiome getPlatformBiome();

    /**
     * Get the color of this biome.
     *
     * @return ARGB color of this biome
     */
    int getColor();

    /**
     * Get the tags this biome holds
     *
     * @return A {@link Set} of String tags this biome holds.
     */
    Set<String> getTags();

    /**
     * Get the namespaced entries of this biome's config {@code tags:}, as written.
     */
    default @NotNull Set<RegistryKey> getPlatformTags() {
        return Set.of();
    }


    /**
     * Get the numeric ID of this biome: dense per pack, from 1, issued by {@code getBiomeTagIndex().register(...)} during pack
     * load.
     *
     * @return The numeric ID.
     */
    int getIntID();
}

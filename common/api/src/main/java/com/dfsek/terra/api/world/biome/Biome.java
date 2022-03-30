/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.biome;


import java.util.Set;

import com.dfsek.terra.api.properties.PropertyHolder;
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
}

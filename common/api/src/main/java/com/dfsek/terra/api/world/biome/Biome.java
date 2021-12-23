/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.biome;


import java.util.Set;

import com.dfsek.terra.api.properties.PropertyHolder;


/**
 * Represents a custom biome
 */
public interface Biome extends PropertyHolder {
    
    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return TerraBiome - The Vanilla biome.
     */
    PlatformBiome getPlatformBiome();
    
    int getColor();
    
    Set<String> getTags();
    
    String getID();
}

/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.single;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class SingleBiomeProvider implements BiomeProvider {
    private final TerraBiome biome;
    
    public SingleBiomeProvider(TerraBiome biome) {
        this.biome = biome;
    }
    
    @Override
    public TerraBiome getBiome(int x, int z, long seed) {
        return biome;
    }
}

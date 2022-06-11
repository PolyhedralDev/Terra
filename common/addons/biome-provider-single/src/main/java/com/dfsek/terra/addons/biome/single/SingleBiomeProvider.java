/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.single;

import java.util.Collections;
import java.util.Optional;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class SingleBiomeProvider implements BiomeProvider {
    private final Biome biome;
    
    public SingleBiomeProvider(Biome biome) {
        this.biome = biome;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return biome;
    }
    
    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return Optional.of(biome);
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return Collections.singleton(biome);
    }
}

/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.single;

import java.util.Collections;

import com.dfsek.terra.api.util.generic.data.types.Maybe;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public record SingleBiomeProvider(Biome biome) implements BiomeProvider {

    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return biome;
    }

    @Override
    public Maybe<Biome> getBaseBiome(int x, int z, long seed) {
        return Maybe.just(biome);
    }

    @Override
    public Iterable<Biome> getBiomes() {
        return Collections.singleton(biome);
    }
}

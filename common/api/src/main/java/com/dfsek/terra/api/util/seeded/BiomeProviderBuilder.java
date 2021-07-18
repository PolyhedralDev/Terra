package com.dfsek.terra.api.util.seeded;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public interface BiomeProviderBuilder extends SeededBuilder<BiomeProvider> {
    BiomeProvider build(long seed);
}

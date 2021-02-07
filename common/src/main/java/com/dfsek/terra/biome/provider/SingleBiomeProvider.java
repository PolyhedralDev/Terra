package com.dfsek.terra.biome.provider;

import com.dfsek.terra.biome.TerraBiome;

public class SingleBiomeProvider implements BiomeProvider {
    private final TerraBiome biome;

    public SingleBiomeProvider(TerraBiome biome) {
        this.biome = biome;
    }

    @Override
    public TerraBiome getBiome(int x, int z) {
        return biome;
    }


}

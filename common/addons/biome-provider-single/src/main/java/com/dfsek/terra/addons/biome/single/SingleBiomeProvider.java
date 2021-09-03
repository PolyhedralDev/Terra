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

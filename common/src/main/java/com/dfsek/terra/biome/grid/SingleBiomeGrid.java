package com.dfsek.terra.biome.grid;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.world.biome.BiomeGrid;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generation.GenerationPhase;

/**
 * BiomeGrid implementation that holds a single biome.
 */
public class SingleBiomeGrid extends BiomeGrid {
    private final TerraBiome biome;

    public SingleBiomeGrid(long seed, TerraBiome biome) {
        super(seed, 0, 0, 1, 1);
        this.biome = biome;
    }

    @Override
    public TerraBiome getBiome(int x, int z, GenerationPhase phase) {
        return biome;
    }

    @Override
    public TerraBiome getBiome(Location l) {
        return biome;
    }
}

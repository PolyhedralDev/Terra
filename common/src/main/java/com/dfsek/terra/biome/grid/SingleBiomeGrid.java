package com.dfsek.terra.biome.grid;

import com.dfsek.terra.api.platform.world.vector.Location;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.BiomeGrid;
import com.dfsek.terra.api.world.generation.GenerationPhase;

/**
 * BiomeGrid implementation that holds a single biome.
 */
public class SingleBiomeGrid extends BiomeGrid {
    private final Biome biome;

    public SingleBiomeGrid(long seed, Biome biome) {
        super(seed, 0, 0, 1, 1);
        this.biome = biome;
    }

    @Override
    public Biome getBiome(int x, int z, GenerationPhase phase) {
        return biome;
    }

    @Override
    public Biome getBiome(Location l) {
        return biome;
    }
}

package com.dfsek.terra.biome.grid;

import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.generation.GenerationPhase;

/**
 * BiomeGrid implementation that holds a single biome.
 */
public class SingleBiomeGrid extends BiomeGrid {
    private final Biome biome;

    public SingleBiomeGrid(World w, Biome biome) {
        super(w, 0, 0, 1, 1);
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

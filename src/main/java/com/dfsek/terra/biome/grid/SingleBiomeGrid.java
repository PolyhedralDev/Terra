package com.dfsek.terra.biome.grid;

import com.dfsek.terra.api.gaea.biome.Biome;
import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import org.bukkit.Location;
import org.bukkit.World;

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

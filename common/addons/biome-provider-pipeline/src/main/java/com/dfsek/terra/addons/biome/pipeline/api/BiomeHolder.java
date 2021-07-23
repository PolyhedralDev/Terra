package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;

public interface BiomeHolder {
    BiomeHolder expand(BiomeExpander expander, long seed);

    void mutate(BiomeMutator mutator, long seed);

    void fill(BiomeSource source, long seed);

    TerraBiome getBiome(int x, int z);

    TerraBiome getBiomeRaw(int x, int z);
}

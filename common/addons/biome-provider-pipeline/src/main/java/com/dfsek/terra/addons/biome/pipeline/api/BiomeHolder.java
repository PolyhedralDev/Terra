package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;

public interface BiomeHolder {
    BiomeHolder expand(BiomeExpander expander);

    void mutate(BiomeMutator mutator);

    void fill(BiomeSource source);

    TerraBiome getBiome(int x, int z);

    TerraBiome getBiomeRaw(int x, int z);
}

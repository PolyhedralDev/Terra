package com.dfsek.terra.biome.pipeline;

import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.expand.BiomeExpander;
import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.biome.pipeline.source.BiomeSource;

public interface BiomeHolder {
    BiomeHolder expand(BiomeExpander expander);

    void mutate(BiomeMutator mutator);

    void fill(BiomeSource source);

    TerraBiome getBiome(int x, int z);

    TerraBiome getBiomeRaw(int x, int z);
}

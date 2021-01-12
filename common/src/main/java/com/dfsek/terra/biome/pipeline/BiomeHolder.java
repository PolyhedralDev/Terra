package com.dfsek.terra.biome.pipeline;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.biome.pipeline.expand.BiomeExpander;
import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.biome.pipeline.source.BiomeSource;

public interface BiomeHolder {
    void expand(BiomeExpander expander);

    void mutate(BiomeMutator mutator);

    void fill(BiomeSource source);

    Biome getBiome(int x, int z);
}

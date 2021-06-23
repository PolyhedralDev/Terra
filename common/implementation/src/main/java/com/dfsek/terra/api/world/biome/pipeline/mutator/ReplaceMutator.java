package com.dfsek.terra.api.world.biome.pipeline.mutator;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeMutator;

public class ReplaceMutator implements BiomeMutator {
    private final String replaceableTag;
    private final ProbabilityCollectionImpl<TerraBiome> replace;
    private final NoiseSampler sampler;

    public ReplaceMutator(String replaceable, ProbabilityCollectionImpl<TerraBiome> replace, NoiseSampler sampler) {
        this.replaceableTag = replaceable;
        this.replace = replace;
        this.sampler = sampler;
    }

    @Override
    public TerraBiome mutate(ViewPoint viewPoint, double x, double z) {
        if(viewPoint.getBiome(0, 0).getTags().contains(replaceableTag)) {
            TerraBiome biome = replace.get(sampler, x, z);
            return biome == null ? viewPoint.getBiome(0, 0) : biome;
        }
        return viewPoint.getBiome(0, 0);
    }
}

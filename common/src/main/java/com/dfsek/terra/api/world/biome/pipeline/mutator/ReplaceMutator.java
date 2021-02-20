package com.dfsek.terra.api.world.biome.pipeline.mutator;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.TerraBiome;

public class ReplaceMutator implements BiomeMutator {
    private final String replaceableTag;
    private final ProbabilityCollection<TerraBiome> replace;
    private final NoiseSampler sampler;

    public ReplaceMutator(String replaceable, ProbabilityCollection<TerraBiome> replace, NoiseSampler sampler) {
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

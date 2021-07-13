package com.dfsek.terra.addons.biome.pipeline.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.Map;

public class ReplaceListMutator implements BiomeMutator {
    private final Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace;
    private final NoiseSampler sampler;
    private final ProbabilityCollection<TerraBiome> replaceDefault;
    private final String defaultTag;

    public ReplaceListMutator(Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace, String defaultTag, ProbabilityCollection<TerraBiome> replaceDefault, NoiseSampler sampler) {
        this.replace = replace;
        this.sampler = sampler;
        this.defaultTag = defaultTag;
        this.replaceDefault = replaceDefault;
    }

    @Override
    public TerraBiome mutate(ViewPoint viewPoint, double x, double z) {
        TerraBiome center = viewPoint.getBiome(0, 0);
        if(replace.containsKey(center)) {
            TerraBiome biome = replace.get(center).get(sampler, x, z);
            return biome == null ? viewPoint.getBiome(0, 0) : biome;
        }
        if(viewPoint.getBiome(0, 0).getTags().contains(defaultTag)) {
            TerraBiome biome = replaceDefault.get(sampler, x, z);
            return biome == null ? viewPoint.getBiome(0, 0) : biome;
        }
        return center;
    }
}

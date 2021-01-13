package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.Position;

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
    public TerraBiome mutate(ViewPoint viewPoint, Position position) {
        return viewPoint.getBiome(0, 0).getTags().contains(replaceableTag) ? replace.get(sampler, position.getX(), position.getY()) : viewPoint.getBiome(0, 0);
    }
}

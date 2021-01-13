package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.Position;

import java.util.Set;

public class ReplaceMutator implements BiomeMutator {
    private final Set<TerraBiome> replaceable;
    private final ProbabilityCollection<TerraBiome> replace;
    private final NoiseSampler sampler;

    public ReplaceMutator(Set<TerraBiome> replaceable, ProbabilityCollection<TerraBiome> replace, NoiseSampler sampler) {
        this.replaceable = replaceable;
        this.replace = replace;
        this.sampler = sampler;
    }

    @Override
    public TerraBiome mutate(ViewPoint viewPoint, Position position) {
        return replaceable.contains(viewPoint.getBiome(0, 0)) ? replace.get(sampler, position.getX(), position.getY()) : viewPoint.getBiome(0, 0);
    }
}

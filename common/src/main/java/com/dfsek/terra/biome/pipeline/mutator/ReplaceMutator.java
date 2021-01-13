package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.biome.pipeline.Position;

import java.util.Set;

public class ReplaceMutator implements BiomeMutator {
    private final Set<Biome> replaceable;
    private final ProbabilityCollection<Biome> replace;
    private final NoiseSampler sampler;

    public ReplaceMutator(Set<Biome> replaceable, ProbabilityCollection<Biome> replace, NoiseSampler sampler) {
        this.replaceable = replaceable;
        this.replace = replace;
        this.sampler = sampler;
    }

    @Override
    public Biome mutate(ViewPoint viewPoint, Position position) {
        return replaceable.contains(viewPoint.getBiome(0, 0)) ? replace.get(sampler, position.getX(), position.getY()) : viewPoint.getBiome(0, 0);
    }
}

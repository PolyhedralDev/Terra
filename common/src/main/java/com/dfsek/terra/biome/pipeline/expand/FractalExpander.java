package com.dfsek.terra.biome.pipeline.expand;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.biome.pipeline.Position;

public class FractalExpander implements BiomeExpander {
    private final NoiseSampler sampler;

    public FractalExpander(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public Biome getBetween(Position center, Biome... others) {
        return others[MathUtil.normalizeIndex(sampler.getNoise(center.getAsVector()), others.length)];
    }
}

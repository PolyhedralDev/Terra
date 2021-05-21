package com.dfsek.terra.api.world.biome.pipeline.expand;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.TerraBiome;

public class FractalExpander implements BiomeExpander {
    private final NoiseSampler sampler;

    public FractalExpander(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public TerraBiome getBetween(double x, double z, TerraBiome... others) {
        return others[MathUtil.normalizeIndex(sampler.getNoise(x, z), others.length)];
    }
}

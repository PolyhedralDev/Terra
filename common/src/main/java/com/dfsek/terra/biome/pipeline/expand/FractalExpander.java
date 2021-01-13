package com.dfsek.terra.biome.pipeline.expand;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.Position;

public class FractalExpander implements BiomeExpander {
    private final NoiseSampler sampler;

    public FractalExpander(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public TerraBiome getBetween(Position center, TerraBiome... others) {
        return others[MathUtil.normalizeIndex(sampler.getNoise(center.getAsVector()), others.length)];
    }
}

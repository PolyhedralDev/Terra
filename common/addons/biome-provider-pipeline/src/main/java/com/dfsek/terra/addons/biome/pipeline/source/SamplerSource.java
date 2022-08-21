package com.dfsek.terra.addons.biome.pipeline.source;

import com.dfsek.terra.addons.biome.pipeline.api.Source;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class SamplerSource implements Source {
    private final ProbabilityCollection<PipelineBiome> biomes;
    private final NoiseSampler sampler;
    
    public SamplerSource(ProbabilityCollection<PipelineBiome> biomes, NoiseSampler sampler) {
        this.biomes = biomes;
        this.sampler = sampler;
    }
    
    @Override
    public PipelineBiome get(long seed, int x, int z) {
        return biomes.get(sampler, x, z, seed);
    }
    
    @Override
    public Iterable<PipelineBiome> getBiomes() {
        return biomes.getContents();
    }
}

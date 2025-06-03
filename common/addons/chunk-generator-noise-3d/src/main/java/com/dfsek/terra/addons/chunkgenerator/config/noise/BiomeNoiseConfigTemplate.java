package com.dfsek.terra.addons.chunkgenerator.config.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;


public class BiomeNoiseConfigTemplate implements ObjectTemplate<BiomeNoiseProperties> {
    @Value("terrain.sampler")
    private @Meta Sampler baseSampler;

    @Value("terrain.sampler-2d")
    @Default
    private @Meta Sampler elevationSampler = Sampler.zero();

    @Value("carving.sampler")
    @Default
    private @Meta Sampler carvingSampler = Sampler.zero();

    @Value("terrain.blend.distance")
    @Default
    private @Meta int blendDistance = 3;

    @Value("terrain.blend.weight")
    @Default
    private @Meta double blendWeight = 1;

    @Value("terrain.blend.step")
    @Default
    private @Meta int blendStep = 4;

    @Value("terrain.blend.weight-2d")
    @Default
    private @Meta double elevationWeight = 1;

    @Override
    public BiomeNoiseProperties get() {
        return new BiomeNoiseProperties(baseSampler, elevationSampler, carvingSampler, blendDistance, blendStep, blendWeight,
            elevationWeight, new ThreadLocalNoiseHolder());
    }
}

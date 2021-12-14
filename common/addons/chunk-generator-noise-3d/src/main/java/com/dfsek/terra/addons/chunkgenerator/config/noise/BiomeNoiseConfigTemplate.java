package com.dfsek.terra.addons.chunkgenerator.config.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public class BiomeNoiseConfigTemplate implements ObjectTemplate<BiomeNoiseProperties> {
    @Value("noise")
    private @Meta NoiseSampler baseSampler;
    
    @Value("elevation.equation")
    @Default
    private @Meta NoiseSampler elevationSampler = NoiseSampler.zero();
    
    @Value("carving.sampler")
    @Default
    private @Meta NoiseSampler carvingSampler = NoiseSampler.zero();
    
    @Value("blend.distance")
    @Default
    private @Meta int blendDistance = 3;
    
    @Value("blend.weight")
    @Default
    private @Meta double blendWeight = 1;
    
    @Value("blend.step")
    @Default
    private @Meta int blendStep = 4;
    
    @Value("elevation.weight")
    @Default
    private @Meta double elevationWeight = 1;
    
    @Override
    public BiomeNoiseProperties get() {
        return new BiomeNoiseProperties(baseSampler, elevationSampler, carvingSampler, blendDistance, blendStep, blendWeight,
                                        elevationWeight);
    }
}

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public abstract class BiomeProviderTemplate implements ObjectTemplate<BiomeProvider> {
    @Value("resolution")
    @Default
    protected int resolution = 1;
    @Value("blend.noise")
    @Default
    protected NoiseSampler blend = NoiseSampler.zero();
    @Value("blend.amplitude")
    @Default
    protected double blendAmp = 0d;
}

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SeededBiomeProvider;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

public abstract class BiomeProviderTemplate implements ObjectTemplate<SeededBiomeProvider>, SeededBiomeProvider {
    @Value("resolution")
    @Default
    protected int resolution = 1;
    @Value("blend.noise")
    @Default
    protected SeededNoiseSampler blend = SeededNoiseSampler.zero(2);
    @Value("blend.amplitude")
    @Default
    protected double blendAmp = 0d;

    @Override
    public SeededBiomeProvider get() {
        return this;
    }
}

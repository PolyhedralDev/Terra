package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.noise.samplers.noise.ConstantSampler;

public abstract class BiomeProviderTemplate implements ObjectTemplate<BiomeProvider.BiomeProviderBuilder>, BiomeProvider.BiomeProviderBuilder {
    @Value("resolution")
    @Default
    protected int resolution = 1;
    @Value("blend.noise")
    @Default
    protected NoiseSeeded blend = new NoiseSeeded() {
        @Override
        public NoiseSampler apply(Long seed) {
            return new ConstantSampler(0);
        }

        @Override
        public int getDimensions() {
            return 2;
        }
    };
    @Value("blend.amplitude")
    @Default
    protected double blendAmp = 0d;
    @Value("type")
    BiomeProvider.Type type;

    @Override
    public BiomeProvider.BiomeProviderBuilder get() {
        return this;
    }
}

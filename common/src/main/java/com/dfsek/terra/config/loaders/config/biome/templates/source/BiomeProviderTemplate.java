package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.ConstantSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.biome.provider.BiomeProvider;
import com.dfsek.terra.registry.config.BiomeRegistry;

public abstract class BiomeProviderTemplate implements ObjectTemplate<BiomeProvider.BiomeProviderBuilder>, BiomeProvider.BiomeProviderBuilder {
    protected final BiomeRegistry registry;
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

    protected BiomeProviderTemplate(BiomeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public BiomeProvider.BiomeProviderBuilder get() {
        return this;
    }
}

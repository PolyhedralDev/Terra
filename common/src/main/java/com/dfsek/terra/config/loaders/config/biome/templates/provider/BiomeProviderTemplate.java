package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.ConstantSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;

/**
 * Configures a biome provider.
 */
public abstract class BiomeProviderTemplate implements ObjectTemplate<BiomeProvider.BiomeProviderBuilder>, BiomeProvider.BiomeProviderBuilder {
    /**
     * Resolution of this provider.
     * A resolution of 1 means that 1 block = 1 sample.
     */
    @Value("resolution")
    @Default
    protected int resolution = 1;

    /**
     * Noise function to use for blending biomes at edges.
     */
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

    /**
     * Amplitude of edge blending, in blocks.
     */
    @Value("blend.amplitude")
    @Default
    protected double blendAmp = 0d;

    @Override
    public BiomeProvider.BiomeProviderBuilder get() {
        return this;
    }
}

package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.ConstantSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;

public abstract class BiomeProviderTemplate implements ObjectTemplate<BiomeProvider.BiomeProviderBuilder>, BiomeProvider.BiomeProviderBuilder {
    @Value("resolution")
    @Default
    protected MetaValue<Integer> resolution = MetaValue.of(1);

    @Value("blend.noise")
    @Default
    protected MetaValue<NoiseSeeded> blend = MetaValue.of(new NoiseSeeded() {
        @Override
        public NoiseSampler apply(Long seed) {
            return new ConstantSampler(0);
        }

        @Override
        public int getDimensions() {
            return 2;
        }
    });

    @Value("blend.amplitude")
    @Default
    protected MetaValue<Double> blendAmp = MetaValue.of(0d);

    @Value("type")
    protected MetaValue<BiomeProvider.Type> type;

    @Override
    public BiomeProvider.BiomeProviderBuilder get() {
        return this;
    }
}

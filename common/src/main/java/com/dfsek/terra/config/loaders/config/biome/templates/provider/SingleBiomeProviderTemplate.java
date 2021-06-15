package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.SingleBiomeProvider;
import com.dfsek.terra.config.builder.BiomeBuilder;

@SuppressWarnings("unused")
public class SingleBiomeProviderTemplate extends BiomeProviderTemplate {
    @Value("biome")
    private MetaValue<BiomeBuilder> biome;

    public SingleBiomeProviderTemplate() {
    }

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome.get().apply(seed));
    }
}

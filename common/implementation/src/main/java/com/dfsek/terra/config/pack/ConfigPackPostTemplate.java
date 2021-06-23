package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;

public class ConfigPackPostTemplate implements ConfigTemplate {
    @Value("biomes")
    private BiomeProvider.BiomeProviderBuilder providerBuilder;

    public BiomeProvider.BiomeProviderBuilder getProviderBuilder() {
        return providerBuilder;
    }
}

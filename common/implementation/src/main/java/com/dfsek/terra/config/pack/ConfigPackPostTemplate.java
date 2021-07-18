package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.util.seeded.BiomeProviderBuilder;

public class ConfigPackPostTemplate implements ConfigTemplate {
    @Value("biomes")
    private BiomeProviderBuilder providerBuilder;

    public BiomeProviderBuilder getProviderBuilder() {
        return providerBuilder;
    }
}

package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.util.seeded.SeededBiomeProvider;

public class ConfigPackPostTemplate implements ConfigTemplate {
    @Value("biomes")
    private SeededBiomeProvider providerBuilder;

    public SeededBiomeProvider getProviderBuilder() {
        return providerBuilder;
    }
}

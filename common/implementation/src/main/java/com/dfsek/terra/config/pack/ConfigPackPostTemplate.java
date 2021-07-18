package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public class ConfigPackPostTemplate implements ConfigTemplate {
    @Value("biomes")
    private SeededBuilder<BiomeProvider> providerBuilder;

    public SeededBuilder<BiomeProvider> getProviderBuilder() {
        return providerBuilder;
    }
}

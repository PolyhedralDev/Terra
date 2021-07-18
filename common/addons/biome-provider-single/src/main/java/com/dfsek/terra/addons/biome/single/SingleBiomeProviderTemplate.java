package com.dfsek.terra.addons.biome.single;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;
import com.dfsek.terra.api.util.seeded.BiomeProviderBuilder;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public class SingleBiomeProviderTemplate implements ObjectTemplate<BiomeProviderBuilder>, BiomeProviderBuilder {
    @Value("biome")
    private SeededTerraBiome biome;

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome.apply(seed));
    }

    @Override
    public BiomeProviderBuilder get() {
        return this;
    }
}

package com.dfsek.terra.addons.biome.single;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;
import com.dfsek.terra.api.util.seeded.SeededBiomeProvider;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public class SingleBiomeProviderTemplate implements ObjectTemplate<SeededBiomeProvider>, SeededBiomeProvider {
    @Value("biome")
    private SeededTerraBiome biome;

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome.build(seed));
    }

    @Override
    public SeededBiomeProvider get() {
        return this;
    }
}

package com.dfsek.terra.addons.biome.single;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public class SingleBiomeProviderTemplate implements ObjectTemplate<BiomeProvider> {
    @Value("biome")
    private TerraBiome biome;

    @Override
    public BiomeProvider get() {
        return new SingleBiomeProvider(biome);
    }
}

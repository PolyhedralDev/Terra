package com.dfsek.terra.addons.biome.single;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.BiomeProviderBuilder;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;

public class SingleBiomeProviderTemplate  implements ObjectTemplate<BiomeProviderBuilder>, BiomeProviderBuilder {
    @Value("biome")
    private BiomeBuilder biome;

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome.apply(seed));
    }

    @Override
    public BiomeProviderBuilder get() {
        return this;
    }
}

package com.dfsek.terra.registry;

import com.dfsek.terra.config.builder.biomegrid.BiomeGridBuilder;
import com.dfsek.terra.config.builder.biomegrid.SingleGridBuilder;

public class BiomeGridRegistry extends TerraRegistry<BiomeGridBuilder> {
    private final BiomeRegistry biomeRegistry;

    public BiomeGridRegistry(BiomeRegistry biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }

    @Override
    public BiomeGridBuilder get(String id) {
        if(id.startsWith("BIOME:")) return new SingleGridBuilder(biomeRegistry.get(id.substring(6)));
        return super.get(id);
    }

    @Override
    public boolean contains(String name) {
        if(name.startsWith("BIOME:")) return biomeRegistry.contains(name.substring(6));
        return super.contains(name);
    }
}

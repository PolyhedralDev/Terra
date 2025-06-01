package com.dfsek.terra.minestom.biome;

import com.dfsek.terra.api.world.biome.PlatformBiome;

import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.world.biome.Biome;


public class MinestomBiome implements PlatformBiome {
    private final DynamicRegistry.Key<Biome> biome;

    public MinestomBiome(DynamicRegistry.Key<Biome> biome) { this.biome = biome; }

    @Override
    public DynamicRegistry.Key<Biome> getHandle() {
        return biome;
    }
}

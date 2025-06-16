package com.dfsek.terra.minestom.biome;

import com.dfsek.terra.api.world.biome.PlatformBiome;

import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.biome.Biome;


public class MinestomBiome implements PlatformBiome {
    private final RegistryKey<Biome> biome;

    public MinestomBiome(RegistryKey<Biome> biome) { this.biome = biome; }

    @Override
    public RegistryKey<Biome> getHandle() {
        return biome;
    }
}

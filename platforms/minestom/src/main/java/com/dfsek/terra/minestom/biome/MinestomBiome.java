package com.dfsek.terra.minestom.biome;

import com.dfsek.terra.api.world.biome.PlatformBiome;

import net.minestom.server.world.biome.Biome;


public class MinestomBiome implements PlatformBiome {
    private final Biome biome;

    public MinestomBiome(Biome biome) { this.biome = biome; }

    @Override
    public Biome getHandle() {
        return biome;
    }
}

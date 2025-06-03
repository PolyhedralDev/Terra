package com.dfsek.terra.minestom.biome;

import net.kyori.adventure.key.Key;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.world.biome.Biome;


public record NativeBiome(Key key, DynamicRegistry.Key<Biome> registry, String id, Biome biome) {
}

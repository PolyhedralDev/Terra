package com.dfsek.terra.minestom.biome;

import net.kyori.adventure.key.Key;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.biome.Biome;


public record UserDefinedBiome(Key key, RegistryKey<Biome> registry, String id, Biome biome) {
}

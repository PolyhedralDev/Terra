package com.dfsek.terra.minestom.biome;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.biome.Biome;


public class UserDefinedBiome {
    private static final DynamicRegistry<Biome> BIOME_REGISTRY = MinecraftServer.getBiomeRegistry();

    private final Key key;
    private final RegistryKey<Biome> registry;
    private final String id;
    private final Biome biome;

    private int registryId = -1;

    public UserDefinedBiome(Key key, RegistryKey<Biome> registry, String id, Biome biome) {
        this.key = key;
        this.registry = registry;
        this.id = id;
        this.biome = biome;
    }

    public Key key() {
        return key;
    }

    public RegistryKey<Biome> registryKey() {
        return registry;
    }

    public String id() {
        return id;
    }

    public Biome biome() {
        return biome;
    }

    public int registryId() {
        if(registryId == -1) {
            registryId = BIOME_REGISTRY.getId(registry);
        }
        return registryId;
    }
}

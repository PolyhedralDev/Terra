package com.dfsek.terra.bukkit.nms.v1_19_R3;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;


public class RegistryFetcher {
    private static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
        CraftServer craftserver = (CraftServer) Bukkit.getServer();
        DedicatedServer dedicatedserver = craftserver.getServer();
        return dedicatedserver
            .registryAccess()
            .registryOrThrow(key);
    }

    public static Registry<Biome> biomeRegistry() {
        return getRegistry(Registries.BIOME);
    }
}

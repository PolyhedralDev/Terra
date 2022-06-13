package com.dfsek.terra.bukkit.nms.v1_18_R2;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;


public class Registries {
    private static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
        CraftServer craftserver = (CraftServer) Bukkit.getServer();
        DedicatedServer dedicatedserver = craftserver.getServer();
        return dedicatedserver
                .registryAccess()
                .registryOrThrow( // getRegistry
                                  key
                                );
    }
    
    public static Registry<Biome> biomeRegistry() {
        return getRegistry(Registry.BIOME_REGISTRY);
    }
    
    public static Registry<StructureSet> structureSet() {
        return getRegistry(Registry.STRUCTURE_SET_REGISTRY);
    }
}

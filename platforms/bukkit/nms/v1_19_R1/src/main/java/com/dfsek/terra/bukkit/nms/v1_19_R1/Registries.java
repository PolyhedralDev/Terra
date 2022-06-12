package com.dfsek.terra.bukkit.nms.v1_19_R1;

import net.minecraft.core.IRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;


public class Registries {
    private static <T> IRegistry<T> getRegistry(ResourceKey<IRegistry<T>> key) {
        CraftServer craftserver = (CraftServer) Bukkit.getServer();
        DedicatedServer dedicatedserver = craftserver.getServer();
        return dedicatedserver
                .aU() // getRegistryManager
                .b( // getRegistry
                    key
                  );
    }
    
    public static IRegistry<BiomeBase> biomeRegistry() {
        return getRegistry(IRegistry.aP);
    }
    
    public static IRegistry<StructureSet> structureSet() {
        return getRegistry(IRegistry.aM);
    }
}

package com.dfsek.terra.bukkit.nms.v1_21_8;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;


public class RegistryFetcher {
    private static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
        return MinecraftServer.getServer()
            .registryAccess()
            .lookupOrThrow(key);
    }

    public static Registry<Biome> biomeRegistry() {
        return getRegistry(Registries.BIOME);
    }

    public static Registry<SoundEvent> soundEventRegistry() {
        return getRegistry(Registries.SOUND_EVENT);
    }
}

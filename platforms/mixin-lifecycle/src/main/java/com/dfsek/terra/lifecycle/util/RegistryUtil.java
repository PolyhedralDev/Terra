package com.dfsek.terra.lifecycle.util;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import com.dfsek.terra.mod.util.MinecraftUtil;

import net.minecraft.util.Identifier;

import com.dfsek.terra.mod.data.Codecs;


public final class RegistryUtil {
    private RegistryUtil() {

    }

    public static void register() {
        MinecraftUtil.registerIntProviderTypes();
        Registry.register(Registries.CHUNK_GENERATOR, new Identifier("terra:terra"), Codecs.MINECRAFT_CHUNK_GENERATOR_WRAPPER);
        Registry.register(Registries.BIOME_SOURCE, new Identifier("terra:terra"), Codecs.TERRA_BIOME_SOURCE);
    }
}

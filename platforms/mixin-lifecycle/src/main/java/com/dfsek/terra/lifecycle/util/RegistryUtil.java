package com.dfsek.terra.lifecycle.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import com.dfsek.terra.mod.data.Codecs;
import com.dfsek.terra.mod.util.MinecraftUtil;


public final class RegistryUtil {
    private RegistryUtil() {

    }

    public static void register() {
        MinecraftUtil.registerIntProviderTypes();
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, Identifier.parse("terra:terra"), Codecs.MINECRAFT_CHUNK_GENERATOR_WRAPPER);
        Registry.register(BuiltInRegistries.BIOME_SOURCE, Identifier.parse("terra:terra"), Codecs.TERRA_BIOME_SOURCE);
    }
}

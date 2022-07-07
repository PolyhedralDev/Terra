package com.dfsek.terra.lifecycle.util;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.dfsek.terra.mod.data.Codecs;


public final class RegistryUtil {
    private RegistryUtil() {

    }
    
    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("terra:terra"), Codecs.MINECRAFT_CHUNK_GENERATOR_WRAPPER);
        Registry.register(Registry.BIOME_SOURCE, new Identifier("terra:terra"), Codecs.TERRA_BIOME_SOURCE);
    }
}

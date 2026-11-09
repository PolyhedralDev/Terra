package com.dfsek.terra.mod.util;

import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.dfsek.terra.api.config.ConfigPack;


public class BiomeUtil {
    public static final Map<Identifier, List<Identifier>>
        TERRA_BIOME_MAP = new HashMap<>();

    public static final Map<Identifier, TagUtil.PlatformTagSource>
        TERRA_BIOME_PLATFORM_TAGS = new HashMap<>();

    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }

    public static Map<Identifier, List<Identifier>> getTerraBiomeMap() {
        return Map.copyOf(TERRA_BIOME_MAP);
    }

    public static Map<Identifier, TagUtil.PlatformTagSource> getTerraBiomePlatformTags() {
        return Map.copyOf(TERRA_BIOME_PLATFORM_TAGS);
    }
}

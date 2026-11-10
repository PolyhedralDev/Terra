package com.dfsek.terra.bukkit.generator;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.MetaPack;


/**
 * Finds a Bukkit world's pack in a metapack by the level stem for the world's environment.
 */
public final class MetaPackWorlds {
    private static final String MINECRAFT = "minecraft:";

    private MetaPackWorlds() {
    }

    public static @Nullable String stem(World.Environment environment) {
        return switch(environment) {
            case NORMAL -> "minecraft:overworld";
            case NETHER -> "minecraft:the_nether";
            case THE_END -> "minecraft:the_end";
            case CUSTOM -> null;
        };
    }

    public static ConfigPack packFor(MetaPack metaPack, World.Environment environment, String worldName) {
        String stem = stem(environment);
        if(stem == null) {
            throw new IllegalArgumentException(
                "Metapack \"" + metaPack.getID() + "\" has no pack for world \"" + worldName + "\": environment " + environment +
                " has no level stem");
        }
        ConfigPack pack = lookup(metaPack.packs(), stem);
        if(pack == null) {
            throw new IllegalArgumentException(
                "Metapack \"" + metaPack.getID() + "\" has no pack for " + stem + " (world \"" + worldName + "\")");
        }
        return pack;
    }

    private static @Nullable ConfigPack lookup(Map<String, ConfigPack> packs, String stem) {
        ConfigPack pack = packs.get(stem);
        return pack != null ? pack : packs.get(stem.substring(MINECRAFT.length()));
    }
}

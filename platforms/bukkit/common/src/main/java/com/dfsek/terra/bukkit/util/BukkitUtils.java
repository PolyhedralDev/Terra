package com.dfsek.terra.bukkit.util;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;


public class BukkitUtils {
    private static final Logger logger = LoggerFactory.getLogger(BukkitUtils.class);

    public static boolean isLiquid(BlockData blockState) {
        Material material = blockState.getMaterial();
        return material == Material.WATER || material == Material.LAVA;
    }

    public static EntityType getEntityType(String id) {
        if(!id.startsWith("minecraft:")) throw new IllegalArgumentException("Invalid entity identifier " + id);
<<<<<<< HEAD
        String entityID = id.toUpperCase(Locale.ROOT).substring(10);
=======

        // TerraScript entity identifiers may append NBT-like data in braces
        // (e.g. minecraft:end_crystal{ShowBottom:0}). Bukkit's EntityType parser
        // cannot handle this, so strip any trailing brace section.
        String raw = id.substring(10);
        int brace = raw.indexOf('{');
        if(brace != -1) raw = raw.substring(0, brace);

        String entityID = raw.toUpperCase(Locale.ROOT)
                             .replace('-', '_');
>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))

        return new BukkitEntityType(switch(entityID) {
            case "END_CRYSTAL" -> org.bukkit.entity.EntityType.END_CRYSTAL;
            case "ENDER_CRYSTAL" -> throw new IllegalArgumentException(
                "Invalid entity identifier " + id); // make sure this issue can't happen the other way around.
            default -> org.bukkit.entity.EntityType.valueOf(entityID);
        });
    }
}

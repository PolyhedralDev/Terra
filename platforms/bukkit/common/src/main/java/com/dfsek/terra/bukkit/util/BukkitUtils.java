package com.dfsek.terra.bukkit.util;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;


public class BukkitUtils {
    public static boolean isLiquid(BlockData blockState) {
        Material material = blockState.getMaterial();
        return material == Material.WATER || material == Material.LAVA;
    }
}

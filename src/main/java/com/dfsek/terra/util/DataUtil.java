package com.dfsek.terra.util;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

public final class DataUtil {
    public static final BlockData STONE = Material.STONE.createBlockData();
    public static final BlockData SNOW = Material.SNOW.createBlockData();
    public static final BlockData WATER = Material.WATER.createBlockData();
    public static final BlockData AIR = Material.AIR.createBlockData();
    public static final Palette<BlockData> BLANK_PALETTE = new RandomPalette<BlockData>(new FastRandom(2403)).add(AIR, 1);
}

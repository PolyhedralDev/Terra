package com.dfsek.terra.util;

import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.api.gaea.world.palette.RandomPalette;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.Sampler;
import com.dfsek.terra.math.MathUtil;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public final class PaletteUtil {
    public static final BlockData WATER = Material.WATER.createBlockData();
    public static final BlockData AIR = Material.AIR.createBlockData();
    public static final Palette<BlockData> BLANK_PALETTE = new RandomPalette<BlockData>(new FastRandom(2403)).add(AIR, 1);

    public static Palette<BlockData> getPalette(int x, int y, int z, BiomeTemplate c, Sampler sampler) {
        PaletteHolder slant = c.getSlantPalette();
        if(slant != null && MathUtil.derivative(sampler, x, y, z) > c.getSlantThreshold()) {
            return slant.getPalette(y);
        }
        return c.getPalette().getPalette(y);
    }
}

package com.dfsek.terra.util;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.Sampler;
import com.dfsek.terra.math.MathUtil;

public final class PaletteUtil {
    public static Palette<BlockData> getPalette(int x, int y, int z, BiomeTemplate c, Sampler sampler) {
        PaletteHolder slant = c.getSlantPalette();
        if(slant != null && MathUtil.derivative(sampler, x, y, z) > c.getSlantThreshold()) {
            return slant.getPalette(y);
        }
        return c.getPalette().getPalette(y);
    }
}

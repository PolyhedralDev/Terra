package com.dfsek.terra.api.util.world;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.world.generation.math.Sampler;

public final class PaletteUtil {
    public static Palette<BlockData> getPalette(int x, int y, int z, BiomeTemplate c, Sampler sampler) {
        PaletteHolder slant = c.getSlantPalette();
        if(slant != null && MathUtil.derivative(sampler, x, y, z) > c.getSlantThreshold()) {
            return slant.getPalette(y);
        }
        return c.getPalette().getPalette(y);
    }
}

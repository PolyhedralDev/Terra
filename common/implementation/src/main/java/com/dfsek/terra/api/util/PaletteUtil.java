package com.dfsek.terra.api.util;

import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.generator.Sampler;
import com.dfsek.terra.api.world.palette.slant.SlantHolder;
import com.dfsek.terra.config.templates.BiomeTemplate;

public final class PaletteUtil {
    public static Palette getPalette(int x, int y, int z, BiomeTemplate c, Sampler sampler) {

        SlantHolder slant = c.getSlant();
        if(slant != null) {
            double slope = MathUtil.derivative(sampler, x, y, z);
            if(slope > slant.getMinSlope()) {
                return slant.getPalette(slope).getPalette(y);
            }
        }

        return c.getPalette().getPalette(y);
    }
}
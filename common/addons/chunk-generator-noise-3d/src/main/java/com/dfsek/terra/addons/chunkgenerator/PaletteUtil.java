package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.generator.Sampler;

public final class PaletteUtil {
    public static Palette getPalette(int x, int y, int z, Generator c, Sampler sampler) {

        /*
        SlantHolder slant = c.getSlant();
        if(slant != null) {
            double slope = MathUtil.derivative(sampler, x, y, z);
            if(slope > slant.getMinSlope()) {
                return slant.getPalette(slope).getPalette(y);
            }
        }

         */

        return c.getPaletteSettings().getPalette(y);
    }
}
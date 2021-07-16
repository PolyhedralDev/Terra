package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.terra.addons.chunkgenerator.palette.PaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolder;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.generator.Sampler;

public final class PaletteUtil {
    public static Palette getPalette(int x, int y, int z, Generator c, Sampler sampler, PaletteInfo paletteInfo) {
        SlantHolder slant = paletteInfo.getSlantHolder();
        if(slant != null) {
            double slope = MathUtil.derivative(sampler, x, y, z);
            if(slope > slant.getMinSlope()) {
                return slant.getPalette(slope).getPalette(y);
            }
        }

        return c.getPaletteSettings().getPalette(y);
    }
}
package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolder;
import com.dfsek.terra.api.world.generator.Palette;

public class BiomePaletteTemplate implements ObjectTemplate<PaletteInfo> {
    @Value("palette")
    private PaletteHolder palette;

    @Value("slant")
    @Default
    private SlantHolder slant = null;

    @Value("ocean.level")
    private int seaLevel;

    @Value("ocean.palette")
    private Palette oceanPalette;

    @Override
    public PaletteInfo get() {
        return new PaletteInfo(palette, slant, oceanPalette, seaLevel);
    }
}

package com.dfsek.terra.addons.biome;

import com.dfsek.terra.addons.biome.holder.PaletteHolder;
import com.dfsek.terra.api.world.biome.PaletteSettings;
import com.dfsek.terra.api.world.generator.Palette;


public class PaletteSettingsImpl implements PaletteSettings {
    private final PaletteHolder palette;
    
    public PaletteSettingsImpl(PaletteHolder palette) {
        this.palette = palette;
    }
    
    @Override
    public Palette getPalette(int y) {
        return palette.getPalette(y);
    }
}

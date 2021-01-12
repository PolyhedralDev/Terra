package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.palette.NoisePalette;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.config.templates.PaletteTemplate;

public class PaletteFactory implements TerraFactory<PaletteTemplate, Palette<BlockData>> {
    @Override
    public Palette<BlockData> build(PaletteTemplate config, TerraPlugin main) {
        Palette<BlockData> palette = new NoisePalette<>(config.getNoise().build(2403), config.getNoise().getDimensions() == 2);
        for(PaletteLayer layer : config.getPalette()) {
            palette.add(layer.getLayer(), layer.getSize());
        }
        return palette;
    }
}

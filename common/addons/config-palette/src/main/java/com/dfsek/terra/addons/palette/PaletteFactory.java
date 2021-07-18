package com.dfsek.terra.addons.palette;

import com.dfsek.terra.addons.palette.palette.NoisePalette;
import com.dfsek.terra.addons.palette.palette.PaletteLayerHolder;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.world.generator.Palette;

public class PaletteFactory implements ConfigFactory<PaletteTemplate, Palette> {
    @Override
    public Palette build(PaletteTemplate config, TerraPlugin main) {
        NoisePalette palette = new NoisePalette(config.getNoise().build(2403L), config.getNoise().getDimensions() == 2);
        for(PaletteLayerHolder layer : config.getPalette()) {
            palette.add(layer.getLayer(), layer.getSize(), layer.getSampler());
        }
        return palette;
    }
}

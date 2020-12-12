package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.gaea.world.Flora;
import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.api.gaea.world.palette.RandomPalette;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.generation.items.flora.TerraFlora;

public class FloraFactory implements TerraFactory<FloraTemplate, Flora> {
    @Override
    public TerraFlora build(FloraTemplate config, TerraPlugin main) {
        Palette<BlockData> palette = new RandomPalette<>(new FastRandom(2403));
        for(PaletteLayer layer : config.getFloraPalette()) {
            palette.add(layer.getLayer(), layer.getSize());
        }
        return new TerraFlora(palette, config.doPhysics(), config.isCeiling(), config.getIrrigable(), config.getSpawnable(), config.getReplaceable(), config.getRotatable(), config.getMaxPlacements(), config.getSearch(), config.isSpawnBlacklist(), config.getIrrigableOffset(), main);
    }
}

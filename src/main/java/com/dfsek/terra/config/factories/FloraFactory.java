package com.dfsek.terra.config.factories;

import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.generation.items.flora.TerraFlora;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

public class FloraFactory implements TerraFactory<FloraTemplate, Flora> {
    @Override
    public TerraFlora build(FloraTemplate config) {
        Palette<BlockData> palette = new RandomPalette<>(new FastRandom(2403));
        for(PaletteLayer layer : config.getFloraPalette()) {
            palette.add(layer.getLayer(), layer.getSize());
        }
        return new TerraFlora(palette, config.doPhysics(), config.isCeiling(), config.getIrrigable(), config.getSpawnable(), config.getReplaceable(), config.getMaxPlacements(), config.getSearch(), config.isSpawnBlacklist(), config.getIrrigableOffset());
    }
}

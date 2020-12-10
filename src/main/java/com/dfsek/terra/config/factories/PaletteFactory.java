package com.dfsek.terra.config.factories;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.config.templates.PaletteTemplate;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;
import org.polydev.gaea.world.palette.SimplexPalette;

public class PaletteFactory implements TerraFactory<PaletteTemplate, Palette<BlockData>> {
    @Override
    public Palette<BlockData> build(PaletteTemplate config, Terra main) {
        Palette<BlockData> palette;
        if(config.isSimplex()) {
            FastNoiseLite noise = new FastNoiseLite((int) config.getSeed());
            noise.setFrequency(config.getFrequency());
            palette = new SimplexPalette<>(noise);
        } else palette = new RandomPalette<>(new FastRandom(config.getSeed()));

        for(PaletteLayer layer : config.getPalette()) {
            palette.add(layer.getLayer(), layer.getSize());
        }
        return palette;
    }
}

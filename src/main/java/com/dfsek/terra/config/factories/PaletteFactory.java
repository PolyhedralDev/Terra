package com.dfsek.terra.config.factories;

import com.dfsek.terra.Terra;
import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.api.gaea.world.palette.RandomPalette;
import com.dfsek.terra.api.gaea.world.palette.SimplexPalette;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.config.templates.PaletteTemplate;
import org.bukkit.block.data.BlockData;

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

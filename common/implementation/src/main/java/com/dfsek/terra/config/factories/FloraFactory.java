package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.api.world.Flora;
import com.dfsek.terra.api.world.palette.NoisePalette;
import com.dfsek.terra.api.world.palette.PaletteImpl;
import com.dfsek.terra.api.world.palette.holder.PaletteLayerHolder;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.world.population.items.flora.TerraFlora;

public class FloraFactory implements ConfigFactory<FloraTemplate, Flora> {
    @Override
    public TerraFlora build(FloraTemplate config, TerraPlugin main) {
        PaletteImpl palette = new NoisePalette(new WhiteNoiseSampler(2403), false);
        for(PaletteLayerHolder layer : config.getFloraPalette()) {
            palette.add(layer.getLayer(), layer.getSize(), layer.getSampler());
        }
        return new TerraFlora(palette, config.doPhysics(), config.isCeiling(), config.getIrrigable(), config.getSpawnable(), config.getReplaceable(), config.getRotatable(), config.getMaxPlacements(), config.getSearch(), config.isSpawnBlacklist(), config.getIrrigableOffset(), main);
    }
}

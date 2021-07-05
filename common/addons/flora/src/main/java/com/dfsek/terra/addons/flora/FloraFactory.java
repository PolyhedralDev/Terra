package com.dfsek.terra.addons.flora;

import com.dfsek.terra.addons.flora.flora.TerraFlora;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.world.Flora;

public class FloraFactory implements ConfigFactory<FloraTemplate, Flora> {
    @Override
    public TerraFlora build(FloraTemplate config, TerraPlugin main) {
        /*PaletteImpl palette = new NoisePalette(new WhiteNoiseSampler(2403), false);
        for(PaletteLayerHolder layer : config.getFloraPalette()) {
            palette.add(layer.getLayer(), layer.getSize(), layer.getSampler());
        }*/
        return new TerraFlora(null, config.doPhysics(), config.isCeiling(), config.getIrrigable(), config.getSpawnable(), config.getReplaceable(), config.getRotatable(), config.getMaxPlacements(), config.getSearch(), config.isSpawnBlacklist(), config.getIrrigableOffset(), main);
    }
}

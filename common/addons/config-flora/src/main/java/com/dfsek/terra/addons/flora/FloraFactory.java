package com.dfsek.terra.addons.flora;

import com.dfsek.terra.addons.flora.flora.TerraFlora;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.world.Flora;

public class FloraFactory implements ConfigFactory<FloraTemplate, Flora> {
    @Override
    public TerraFlora build(FloraTemplate config, TerraPlugin main) {
        return new TerraFlora(config.getLayers(), config.doPhysics(), config.isCeiling(), config.getIrrigable(), config.getSpawnable(), config.getReplaceable(), config.getRotatable(), config.getMaxPlacements(), config.getSearch(), config.isSpawnBlacklist(), config.getIrrigableOffset(), main, config.getNoiseDistribution().apply(2403L));
    }
}

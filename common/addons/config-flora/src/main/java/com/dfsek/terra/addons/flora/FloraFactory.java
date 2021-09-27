package com.dfsek.terra.addons.flora;

import com.dfsek.terra.addons.flora.flora.gen.TerraFlora;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.structure.Structure;


public class FloraFactory implements ConfigFactory<FloraTemplate, Structure> {
    @Override
    public TerraFlora build(FloraTemplate config, Platform platform) {
        return new TerraFlora(config.getLayers(), config.doPhysics(), config.isCeiling(),
                              config.getRotatable(),
                              config.getNoiseDistribution(), config.getID());
    }
}

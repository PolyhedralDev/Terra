/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

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

/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore;

import com.dfsek.tectonic.api.exception.LoadException;

import com.dfsek.terra.addons.ore.ores.VanillaOre;
import com.dfsek.terra.addons.ore.ores.VanillaScatteredOre;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.structure.Structure;


public class ScatteredOreFactory implements ConfigFactory<ScatteredOreTemplate, Structure> {
    @Override
    public Structure build(ScatteredOreTemplate config, Platform platform) throws LoadException {
        BlockState m = config.getMaterial();
        return new VanillaScatteredOre(m, config.getSize(), config.getReplaceable(), config.doPhysics(), config.isExposed(),
                                       config.getMaterialOverrides(), config.getSpread());
    }
}

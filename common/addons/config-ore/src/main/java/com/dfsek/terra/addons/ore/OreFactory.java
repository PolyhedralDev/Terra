/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore;

import com.dfsek.terra.addons.ore.ores.VanillaOre;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.structure.Structure;


public class OreFactory implements ConfigFactory<OreTemplate, Structure> {
    @Override
    public VanillaOre build(OreTemplate config, Platform platform) {
        BlockState m = config.getMaterial();
        return new VanillaOre(m, config.getSize(), config.getReplaceable(), config.doPhysics(), config.isExposed(),
                              config.getMaterialOverrides());
    }
}

/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;


public class StructureFactory implements ConfigFactory<StructureTemplate, ConfiguredStructure> {
    @Override
    public ConfiguredStructure build(StructureTemplate config, Platform platform) {
        return new TerraStructure(config.getStructures(), config.getY(), config.getSpawn(), config.getID());
    }
}

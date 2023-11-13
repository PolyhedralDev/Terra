/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class ScatteredOreConfigType implements ConfigType<ScatteredOreTemplate, Structure> {
    public static final TypeKey<Structure> ORE_TYPE_TOKEN = new TypeKey<>() {
    };
    private final ScatteredOreFactory factory = new ScatteredOreFactory();

    @Override
    public ScatteredOreTemplate getTemplate(ConfigPack pack, Platform platform) {
        return new ScatteredOreTemplate();
    }

    @Override
    public ConfigFactory<ScatteredOreTemplate, Structure> getFactory() {
        return factory;
    }

    @Override
    public TypeKey<Structure> getTypeKey() {
        return ORE_TYPE_TOKEN;
    }
}

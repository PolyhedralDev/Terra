/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.flora;

import java.util.function.Supplier;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class FloraConfigType implements ConfigType<FloraTemplate, Structure> {
    public static final TypeKey<Structure> FLORA_TYPE_TOKEN = new TypeKey<>() {
    };
    private final FloraFactory factory = new FloraFactory();
    
    @Override
    public FloraTemplate getTemplate(ConfigPack pack, Platform platform) {
        return new FloraTemplate();
    }
    
    @Override
    public ConfigFactory<FloraTemplate, Structure> getFactory() {
        return factory;
    }
    
    @Override
    public TypeKey<Structure> getTypeKey() {
        return FLORA_TYPE_TOKEN;
    }
}

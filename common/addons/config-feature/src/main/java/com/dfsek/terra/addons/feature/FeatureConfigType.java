/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.structure.feature.Feature;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class FeatureConfigType implements ConfigType<FeatureTemplate, Feature> {
    public static final TypeKey<Feature> FEATURE_TYPE_KEY = new TypeKey<>() {
    };
    
    private final FeatureFactory factory = new FeatureFactory();
    
    @Override
    public FeatureTemplate getTemplate(ConfigPack pack, Platform platform) {
        return new FeatureTemplate();
    }
    
    @Override
    public ConfigFactory<FeatureTemplate, Feature> getFactory() {
        return factory;
    }
    
    @Override
    public TypeKey<Feature> getTypeKey() {
        return FEATURE_TYPE_KEY;
    }
}

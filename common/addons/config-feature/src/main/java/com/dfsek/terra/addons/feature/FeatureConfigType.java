package com.dfsek.terra.addons.feature;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.structure.feature.Feature;
import com.dfsek.terra.api.util.reflection.TypeKey;

import java.util.function.Supplier;

public class FeatureConfigType implements ConfigType<FeatureTemplate, Feature> {
    public static final TypeKey<Feature> FEATURE_TYPE_KEY = new TypeKey<>() {};

    private final FeatureFactory factory = new FeatureFactory();

    @Override
    public FeatureTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
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

    @Override
    public Supplier<OpenRegistry<Feature>> registrySupplier(ConfigPack pack) {
        return pack.getRegistryFactory()::create;
    }
}

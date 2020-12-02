package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.structure.features.Feature;

import java.lang.reflect.Type;

public class StructureFeatureLoader implements TypeLoader<Feature> {
    @Override
    public Feature load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        return null;
    }
}

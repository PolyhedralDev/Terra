package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.math.GridSpawn;


@SuppressWarnings("unchecked")
public class GridSpawnLoader implements TypeLoader<StructureSpawn> {
    @Override
    public StructureSpawn load(AnnotatedType type, Object o, ConfigLoader configLoader) {
        Map<String, Integer> map = (Map<String, Integer>) o;
        return new GridSpawn(map.get("width"), map.get("padding"), map.getOrDefault("salt", 0));
    }
}

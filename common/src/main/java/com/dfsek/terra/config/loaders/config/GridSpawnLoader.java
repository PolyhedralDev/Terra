package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.procgen.GridSpawn;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GridSpawnLoader implements TypeLoader<GridSpawn> {
    @Override
    public GridSpawn load(Type type, Object o, ConfigLoader configLoader) {
        Map<String, Integer> map = (Map<String, Integer>) o;
        return new GridSpawn(map.get("width"), map.get("padding"), map.getOrDefault("seed", 0));
    }
}

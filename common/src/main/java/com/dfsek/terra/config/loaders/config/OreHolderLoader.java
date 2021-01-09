package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.population.items.ores.Ore;
import com.dfsek.terra.population.items.ores.OreConfig;
import com.dfsek.terra.population.items.ores.OreHolder;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class OreHolderLoader implements TypeLoader<OreHolder> {
    @Override
    public OreHolder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        OreHolder holder = new OreHolder();
        Map<String, Object> map = (Map<String, Object>) o;

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            holder.add((Ore) configLoader.loadType(Ore.class, entry.getKey()), (OreConfig) configLoader.loadType(OreConfig.class, entry.getValue()));
        }

        return holder;
    }
}

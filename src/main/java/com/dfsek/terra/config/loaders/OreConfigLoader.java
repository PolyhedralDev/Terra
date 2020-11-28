package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.generation.items.ores.OreConfig;
import org.polydev.gaea.math.Range;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class OreConfigLoader implements TypeLoader<OreConfig> {
    @Override
    public OreConfig load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Integer> map = (Map<String, Integer>) o;
        Range amount = new Range(map.get("min"), map.get("max"));
        Range height = new Range(map.get("min-height"), map.get("max-height"));
        return new OreConfig(amount, height);
    }
}

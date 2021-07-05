package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.range.ConstantRange;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.addons.ore.ores.OreConfig;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class OreConfigLoader implements TypeLoader<OreConfig> {
    @Override
    public OreConfig load(Type type, Object o, ConfigLoader configLoader) {
        Map<String, Integer> map = (Map<String, Integer>) o;
        Range amount = new ConstantRange(map.get("min"), map.get("max"));
        Range height = new ConstantRange(map.get("min-height"), map.get("max-height"));
        return new OreConfig(amount, height);
    }
}

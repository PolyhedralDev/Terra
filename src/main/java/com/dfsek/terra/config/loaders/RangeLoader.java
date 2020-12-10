package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.gaea.math.Range;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class RangeLoader implements TypeLoader<Range> {
    @Override
    public Range load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Integer> map = (Map<String, Integer>) o;
        return new Range(map.get("min"), map.get("max"));
    }
}

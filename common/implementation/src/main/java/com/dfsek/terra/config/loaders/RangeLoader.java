package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class RangeLoader implements TypeLoader<Range> {
    @Override
    public Range load(AnnotatedType type, Object o, ConfigLoader configLoader) throws LoadException {
        if(o instanceof Map) {
            Map<String, Integer> map = (Map<String, Integer>) o;
            return new ConstantRange(map.get("min"), map.get("max"));
        } else {
            int h = configLoader.loadType(Integer.class, o);
            return new ConstantRange(h, h+1);
        }
    }
}

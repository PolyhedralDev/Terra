package com.dfsek.terra.api.world.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public record RawValue(Object value) {
    public RawValue {
        value = copy(value);
    }

    private static Object copy(Object o) {
        if(o == null || o instanceof String || o instanceof Number || o instanceof Boolean) return o;
        if(o instanceof List<?> list) {
            List<Object> out = new ArrayList<>(list.size());
            for(Object e : list) out.add(copy(e));
            return Collections.unmodifiableList(out);
        }
        if(o instanceof Map<?, ?> map) {
            Map<String, Object> out = new LinkedHashMap<>();
            for(Map.Entry<?, ?> e : map.entrySet()) {
                if(!(e.getKey() instanceof String key)) {
                    throw new IllegalArgumentException("unsupported value: map keys must be strings");
                }
                out.put(key, copy(e.getValue()));
            }
            return Collections.unmodifiableMap(out);
        }
        throw new IllegalArgumentException("unsupported value");
    }
}

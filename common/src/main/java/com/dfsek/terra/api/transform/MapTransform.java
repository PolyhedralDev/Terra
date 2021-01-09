package com.dfsek.terra.api.transform;

import java.util.HashMap;
import java.util.Map;

public class MapTransform<F, T> implements Transform<F, T> {
    private final Map<F, T> map;

    public MapTransform(Map<F, T> map) {
        this.map = map;
    }

    public MapTransform() {
        this.map = new HashMap<>();
    }

    public MapTransform<F, T> add(F from, T to) {
        map.put(from, to);
        return this;
    }

    public MapTransform<F, T> remove(F from) {
        map.remove(from);
        return this;
    }

    @Override
    public T transform(F input) throws TransformException {
        if(!map.containsKey(input)) throw new TransformException("No key matching " + input.toString() + " found in map.");
        return map.get(input);
    }
}

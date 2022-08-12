package com.dfsek.terra.addons.image.converter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ExactColorConverter<T> implements ColorConverter<T> {
    private final Map<Integer, T> map;
    
    private final T fallback;
    
    public ExactColorConverter(Map<Integer, T> map, T fallback) {
        this.map = map;
        this.fallback = fallback;
    }
    
    @Override
    public T apply(Integer color) {
        T lookup = map.get(color);
        if(lookup != null) return lookup;
        return fallback;
    }
    
    @Override
    public Iterable<T> getEntries() {
        Set<T> entries = new HashSet<>(map.values());
        entries.add(fallback);
        return entries;
    }
}

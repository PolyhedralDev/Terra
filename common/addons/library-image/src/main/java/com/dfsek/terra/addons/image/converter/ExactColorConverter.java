package com.dfsek.terra.addons.image.converter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.addons.image.util.ColorUtil;
import com.dfsek.terra.addons.image.util.MapUtil;


public class ExactColorConverter<T> implements ColorConverter<T> {
    private final Map<Integer, T> map;
    
    private final T fallback;
    
    private final boolean ignoreAlpha;
    
    public ExactColorConverter(Map<Integer, T> map, T fallback, boolean ignoreAlpha) {
        if (ignoreAlpha) {
            map = MapUtil.mapKeys(map, ColorUtil::zeroAlpha);
        }
        this.map = map;
        this.fallback = fallback;
        this.ignoreAlpha = ignoreAlpha;
    }
    
    @Override
    public T apply(Integer color) {
        if (ignoreAlpha) {
            color = ColorUtil.zeroAlpha(color);
        }
        T lookup = map.get(color);
        return lookup != null ? lookup : fallback;
    }
    
    @Override
    public Iterable<T> getEntries() {
        Set<T> entries = new HashSet<>(map.values());
        entries.add(fallback);
        return entries;
    }
}

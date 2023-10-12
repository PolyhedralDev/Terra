package com.dfsek.terra.addons.chunkgenerator.palette;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class DoubleNavigableHolder<T> {
    
    private final NavigableMap<Double, T> map;
    
    public DoubleNavigableHolder(Map<Double, T> inputMap) {
        NavigableMap<Double, T> map = new TreeMap<>(inputMap);
        map.put(Double.MAX_VALUE, map.lastEntry().getValue());
        this.map = map;
    }
    
    public T get(double threshold) {
        return map.ceilingEntry(threshold).getValue();
    }
    
    enum Method {
        CEILING,
        FLOOR,
        CLOSEST
    }
    
    public class Single extends DoubleNavigableHolder<T> {
    
        public Single(Map<Double, T> inputMap) {
            super(inputMap);
        }
    }
}

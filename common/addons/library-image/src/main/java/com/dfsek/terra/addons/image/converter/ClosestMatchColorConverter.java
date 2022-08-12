package com.dfsek.terra.addons.image.converter;

import java.util.Map;

import com.dfsek.terra.addons.image.util.ColorUtil;

public class ClosestMatchColorConverter<T> implements ColorConverter<T> {

    private final Map<Integer, T> map;

    private final Integer[] colors;

    public ClosestMatchColorConverter(Map<Integer, T> map) {
        this.map = map;
        this.colors = map.keySet().toArray(new Integer[0]);
    }

    @Override
    public T apply(Integer color) {
        int closest = 0;
        int smallestDistance = Integer.MAX_VALUE;
        for(int compare : colors) {
            int distance = ColorUtil.distance(color, compare);
            if(distance == 0) {
                closest = compare;
                break;
            } else if(distance < smallestDistance) {
                smallestDistance = distance;
                closest = compare;
            }
        }
        return map.get(closest);
    }

    @Override
    public Iterable<T> getEntries() {
        return map.values();
    }
}

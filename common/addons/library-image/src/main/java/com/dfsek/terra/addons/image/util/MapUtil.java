package com.dfsek.terra.addons.image.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MapUtil {

    private MapUtil() {}
    
    /**
     * Utility method for applying transformations on a map's keys.
     */
    public static <O, N, T> Map<N, T> mapKeys(Map<O, T> map, Function<O, N> mappingFunction) {
        return map
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> mappingFunction.apply(e.getKey()),
                        Entry::getValue
                ));
    }
}

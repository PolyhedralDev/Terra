package com.dfsek.terra.addons.image.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public class MapUtil {

    private MapUtil() {}
    
    /**
     * Utility method for converting config keys into integers,
     * used because Tectonic does not accept non string keys.
     */
    public static <T> Map<Integer, T> convertKeysToInt(Map<String, T> map) {
        return map.entrySet().stream()
                  .collect(Collectors.toMap(
                      e -> Integer.decode(e.getKey()),
                      Entry::getValue
                  ));
    }
}

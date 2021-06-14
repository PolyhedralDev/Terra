package com.dfsek.terra.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class MapUtil {
    public static <K1, V1, K2, V2, M extends Map<K2, V2>> M remap(Function<K1, K2> keys, Function<V1, V2> values, Map<K1, V1> in, Supplier<M> newMap) {
        M map = newMap.get();
        in.forEach((k, v) -> map.put(keys.apply(k), values.apply(v)));
        return map;
    }

    public static <K1, V1, K2, V2> Map<K2, V2> remap(Function<K1, K2> keys, Function<V1, V2> values, Map<K1, V1> in) {
        return remap(keys, values, in, HashMap::new);
    }

    public static <K, V1, V2> Map<K, V2> remapValues(Function<V1, V2> values, Map<K, V1> in) {
        return remap(Function.identity(), values, in);
    }
}

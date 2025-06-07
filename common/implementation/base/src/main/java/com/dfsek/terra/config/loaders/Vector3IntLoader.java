package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;

import com.dfsek.seismic.type.vector.Vector3Int;

public class Vector3IntLoader implements TypeLoader<Vector3Int> {
    
    @SuppressWarnings("unchecked")
    @Override
    public Vector3Int load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader,
                           DepthTracker depthTracker) throws LoadException {
        if (o instanceof List) {
            List<Integer> list = (List<Integer>) o;
            return Vector3Int.of(list.get(0), list.get(1), list.get(2));
        }
        else {
            Map<String, Integer> map = (Map<String, Integer>) o;
            return Vector3Int.of(map.get("x"), map.get("y"), map.get("z"));
        }
    }
}


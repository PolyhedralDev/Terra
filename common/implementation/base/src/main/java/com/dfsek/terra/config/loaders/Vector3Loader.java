package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.util.vector.Vector3;


public class Vector3Loader implements TypeLoader<Vector3> {
    
    @SuppressWarnings("unchecked")
    @Override
    public Vector3 load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader,
                           DepthTracker depthTracker) throws LoadException {
        if (o instanceof List) {
            List<Double> list = (List<Double>) o;
            return Vector3.of(list.get(0), list.get(1), list.get(2));
        }
        else {
            Map<String, Double> map = (Map<String, Double>) o;
            return Vector3.of(map.get("x"), map.get("y"), map.get("z"));
        }
    }
}


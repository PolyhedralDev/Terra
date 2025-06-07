package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.color.Color;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;


public class RGBLikeLoader implements TypeLoader<RGBLike> {
    @Override
    public RGBLike load(
        @NotNull AnnotatedType annotatedType,
        @NotNull Object o,
        @NotNull ConfigLoader configLoader,
        DepthTracker depthTracker
    ) throws LoadException {
        if(!(o instanceof @Subst("a:o") Integer value)) {
            throw new LoadException("Value is not an integer", depthTracker);
        }
        return new Color(value);
    }
}

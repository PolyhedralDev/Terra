package com.dfsek.terra.minestom.biome;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

import com.dfsek.terra.api.world.biome.PlatformBiome;

import net.kyori.adventure.key.Key;
import net.minestom.server.registry.DynamicRegistry;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

public class MinestomBiomeLoader implements TypeLoader<PlatformBiome> {
    @Override
    public PlatformBiome load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader,
                              DepthTracker depthTracker) throws LoadException {
        @Subst("name:value")
        String id = (String) o;
        Key key = Key.key(id);
        return new MinestomBiome(DynamicRegistry.Key.of(key));
    }
}
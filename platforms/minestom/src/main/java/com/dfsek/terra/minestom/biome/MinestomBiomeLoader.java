package com.dfsek.terra.minestom.biome;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

import com.dfsek.terra.api.world.biome.PlatformBiome;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

public class MinestomBiomeLoader implements TypeLoader<PlatformBiome> {
    private final DynamicRegistry<Biome> biomeRegistry = MinecraftServer.getBiomeRegistry();

    @Override
    public PlatformBiome load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader,
                              DepthTracker depthTracker) throws LoadException {
        String id = (String) o;
        Key key = Key.key(id);
        Biome biome = biomeRegistry.get(key);
        if(biome == null) throw new LoadException("Biome %s does not exist in registry".formatted(id), depthTracker);
        return new MinestomBiome(biome);
    }
}
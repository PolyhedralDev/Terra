package com.dfsek.terra.addons.biome.extrusion.config;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

import com.dfsek.terra.addons.biome.extrusion.api.ReplaceableBiome;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.Biome;


public class ReplaceableBiomeLoader implements TypeLoader<ReplaceableBiome> {
    private final Registry<Biome> biomeRegistry;
    
    public ReplaceableBiomeLoader(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }
    
    @Override
    public ReplaceableBiome load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker)
    throws LoadException {
        if(c.equals("SELF")) return ReplaceableBiome.self();
        return biomeRegistry
                .getByID((String) c)
                .map(ReplaceableBiome::of)
                .orElseThrow(() -> new LoadException("No such biome: " + c, depthTracker));
    }
}

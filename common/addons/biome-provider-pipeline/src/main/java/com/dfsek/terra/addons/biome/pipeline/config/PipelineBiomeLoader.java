package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.Biome;


public class PipelineBiomeLoader implements TypeLoader<PipelineBiome> {
    private final Registry<Biome> biomeRegistry;
    
    public PipelineBiomeLoader(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }
    
    @Override
    public PipelineBiome load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker)
    throws LoadException {
        if(c.equals("SELF")) return PipelineBiome.self();
        return biomeRegistry
                .getByID((String) c)
                .map(PipelineBiome::from)
                .orElseGet(() -> PipelineBiome.placeholder((String) c));
    }
}

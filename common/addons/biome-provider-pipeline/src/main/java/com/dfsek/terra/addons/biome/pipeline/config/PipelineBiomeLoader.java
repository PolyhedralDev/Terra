package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.concurrent.ConcurrentHashMap;

import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.tag.BiomeTagIndex;


public class PipelineBiomeLoader implements TypeLoader<PipelineBiome> {
    private final Registry<Biome> biomeRegistry;

    private final BiomeTagIndex tagIndex;
    private final ConcurrentHashMap<String, PipelineBiome> placeholders = new ConcurrentHashMap<>();

    public PipelineBiomeLoader(Registry<Biome> biomeRegistry, BiomeTagIndex tagIndex) {
        this.biomeRegistry = biomeRegistry;
        this.tagIndex = tagIndex;
    }

    @Override
    public PipelineBiome load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker)
    throws LoadException {
        if(c.equals("SELF")) return PipelineBiome.self();
        return biomeRegistry
            .getByID((String) c)
            .map(PipelineBiome::from)
            .orElseGet(() -> placeholders.computeIfAbsent((String) c, id -> PipelineBiome.placeholder(id, tagIndex)));
    }
}

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeDelegate;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.Biome;


public class BiomeDelegateLoader implements TypeLoader<BiomeDelegate> {
    private final Registry<Biome> biomeRegistry;
    
    public BiomeDelegateLoader(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }
    
    @Override
    public BiomeDelegate load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader) throws LoadException {
        if(c == "SELF") return BiomeDelegate.self();
        return biomeRegistry
                .get((String) c)
                .map(BiomeDelegate::from)
                .orElseGet(() -> BiomeDelegate.ephemeral((String) c));
    }
}

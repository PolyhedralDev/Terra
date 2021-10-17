package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;

import com.dfsek.terra.addons.biome.pipeline.BiomePipelineProvider;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomeProviderLoader implements TypeLoader<BiomeProvider> {
    @Override
    public BiomeProvider load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        return loader.loadType(BiomePipelineProvider.class, c); // TODO: actually implement this lol
    }
}

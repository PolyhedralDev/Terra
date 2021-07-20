package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

import java.lang.reflect.AnnotatedType;

public class BiomeProviderLoader implements TypeLoader<BiomeProvider> {
    @Override
    public BiomeProvider load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        return loader.loadType(BiomePipelineTemplate.class, c).get(); // TODO: actually implement this lol
    }
}

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.util.seeded.BiomeProviderBuilder;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

public class BiomeProviderBuilderLoader implements TypeLoader<BiomeProviderBuilder> {
    @Override
    public BiomeProviderBuilder load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        return loader.loadType(BiomePipelineTemplate.class, c); // TODO: actually implement this lol
    }
}

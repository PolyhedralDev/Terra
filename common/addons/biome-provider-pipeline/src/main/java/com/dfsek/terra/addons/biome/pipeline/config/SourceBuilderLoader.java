package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.util.seeded.SourceSeeded;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SourceBuilderLoader implements TypeLoader<SourceSeeded> {
    @Override
    public SourceSeeded load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> source = (Map<String, Object>) c;

        BiomeSource.Type type = loader.loadType(BiomeSource.Type.class, source.get("type"));

        if(type == BiomeSource.Type.NOISE) {
            return loader.loadType(NoiseSourceTemplate.class, source);
        }
        throw new LoadException("No such loader type: " + type);
    }
}

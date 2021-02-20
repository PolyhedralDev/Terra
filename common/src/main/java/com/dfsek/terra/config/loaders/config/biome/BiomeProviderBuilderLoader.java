package com.dfsek.terra.config.loaders.config.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.config.loaders.config.biome.templates.source.BiomePipelineTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.source.ImageProviderTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.source.SingleBiomeProviderTemplate;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class BiomeProviderBuilderLoader implements TypeLoader<BiomeProvider.BiomeProviderBuilder> {

    public BiomeProviderBuilderLoader() {
    }

    @Override
    public BiomeProvider.BiomeProviderBuilder load(Type t, Object c, ConfigLoader loader) throws LoadException { // TODO: clean this up
        Map<String, Object> map = (Map<String, Object>) c;

        switch(loader.loadClass(BiomeProvider.Type.class, map.get("type"))) {
            case IMAGE:
                return loader.loadClass(ImageProviderTemplate.class, map);
            case PIPELINE:
                return loader.loadClass(BiomePipelineTemplate.class, map);
            case SINGLE:
                return loader.loadClass(SingleBiomeProviderTemplate.class, map);
        }

        throw new LoadException("No such biome provider type: " + map.get("type"));
    }
}

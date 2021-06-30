package com.dfsek.terra.addons.noise.config;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.seeded.NoiseProvider;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoiseSamplerBuilderLoader implements TypeLoader<NoiseSeeded> {
    private final Registry<NoiseProvider> noiseRegistry;

    public NoiseSamplerBuilderLoader(Registry<NoiseProvider> noiseRegistry) {
        this.noiseRegistry = noiseRegistry;
    }

    @Override
    public NoiseSeeded load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;
        try {
            ObjectTemplate<NoiseSeeded> normalizerTemplate = noiseRegistry.get(((String) map.get("type")).toUpperCase(Locale.ROOT)).get();
            loader.load(normalizerTemplate, new Configuration(map));
            return normalizerTemplate.get();
        } catch(ConfigException e) {
            throw new LoadException("Unable to load noise function: ", e);
        }
    }
}

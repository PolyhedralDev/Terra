package com.dfsek.terra.addons.noise.config;

import com.dfsek.tectonic.config.MapConfiguration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.provider.NoiseProvider;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

import java.lang.reflect.AnnotatedType;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoiseSamplerBuilderLoader implements TypeLoader<SeededNoiseSampler> {
    private final Registry<NoiseProvider> noiseRegistry;

    public NoiseSamplerBuilderLoader(Registry<NoiseProvider> noiseRegistry) {
        this.noiseRegistry = noiseRegistry;
    }

    @Override
    public SeededNoiseSampler load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;
        try {
            if(!noiseRegistry.contains((String) map.get("type"))) {
                throw new LoadException("No such noise function: " + map.get("type"));
            }
            ObjectTemplate<SeededNoiseSampler> normalizerTemplate = noiseRegistry.get(((String) map.get("type")).toUpperCase(Locale.ROOT)).get();
            loader.load(normalizerTemplate, new MapConfiguration(map));
            return normalizerTemplate.get();
        } catch(ConfigException e) {
            throw new LoadException("Unable to load noise function: ", e);
        }
    }
}

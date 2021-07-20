package com.dfsek.terra.addons.noise.config;

import com.dfsek.tectonic.config.MapConfiguration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

import java.lang.reflect.AnnotatedType;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class NoiseSamplerBuilderLoader implements TypeLoader<NoiseSampler> {
    private final Registry<Supplier<ObjectTemplate<NoiseSampler>>> noiseRegistry;

    public NoiseSamplerBuilderLoader(Registry<Supplier<ObjectTemplate<NoiseSampler>>> noiseRegistry) {
        this.noiseRegistry = noiseRegistry;
    }

    @Override
    public NoiseSampler load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;
        try {
            if(!noiseRegistry.contains((String) map.get("type"))) {
                throw new LoadException("No such noise function: " + map.get("type"));
            }
            ObjectTemplate<NoiseSampler> normalizerTemplate = noiseRegistry.get(((String) map.get("type"))).get();
            loader.load(normalizerTemplate, new MapConfiguration(map));
            return normalizerTemplate.get();
        } catch(ConfigException e) {
            throw new LoadException("Unable to load noise function: ", e);
        }
    }
}

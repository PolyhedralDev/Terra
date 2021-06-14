package com.dfsek.terra.config.loaders.config.sampler;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.registry.config.NoiseRegistry;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class NoiseSamplerBuilderLoader implements TypeLoader<NoiseSeeded> {
    private final NoiseRegistry noiseRegistry;

    public NoiseSamplerBuilderLoader(NoiseRegistry noiseRegistry) {
        this.noiseRegistry = noiseRegistry;
    }

    @Override
    public NoiseSeeded load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;
        try {
            Template typeTemplate = new Template();
            Configuration configuration = new Configuration(map);
            loader.load(typeTemplate, configuration);
            ObjectTemplate<NoiseSeeded> normalizerTemplate = typeTemplate.get().get();
            loader.load(normalizerTemplate, configuration);
            return normalizerTemplate.get();
        } catch(ConfigException e) {
            throw new LoadException("Unable to load noise function: ", e);
        }
    }

    @SuppressWarnings("unused")
    private class Template implements ObjectTemplate<Supplier<ObjectTemplate<NoiseSeeded>>> {
        @Value("type")
        private MetaValue<String> type;


        @Override
        public Supplier<ObjectTemplate<NoiseSeeded>> get() {
            return noiseRegistry.get(type.get().toUpperCase(Locale.ROOT));
        }
    }
}

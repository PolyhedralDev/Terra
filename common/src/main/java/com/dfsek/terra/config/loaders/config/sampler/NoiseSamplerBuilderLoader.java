package com.dfsek.terra.config.loaders.config.sampler;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.sampler.templates.DomainWarpTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.FastNoiseTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.ImageSamplerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.NormalizerTemplate;
import com.dfsek.terra.registry.config.NormalizerRegistry;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoiseSamplerBuilderLoader implements TypeLoader<NoiseSeeded> {
    private final NormalizerRegistry normalizerRegistry;

    public NoiseSamplerBuilderLoader(NormalizerRegistry normalizerRegistry) {
        this.normalizerRegistry = normalizerRegistry;
    }

    @Override
    public NoiseSeeded load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;

        String samplerType = "NOISE";

        if(map.containsKey("sampler-type")) {
            samplerType = map.get("sampler-type").toString();
        }

        switch(samplerType) {
            case "NOISE":
                return loader.loadClass(FastNoiseTemplate.class, map).get();
            case "NORMALIZER":
                try {
                    NormalizerTemplate<?> normalizerTemplate = normalizerRegistry.get((String) map.get("type")).get();
                    loader.load(normalizerTemplate, new Configuration(map));
                    return normalizerTemplate;
                } catch(ConfigException e) {
                    throw new LoadException("Unable to load normalizer: ", e);
                }
            case "IMAGE": {
                return loader.loadClass(ImageSamplerTemplate.class, map).get();
            }
            case "DOMAIN_WARP":
                return loader.loadClass(DomainWarpTemplate.class, map).get();
        }

        throw new LoadException("No such noise sampler type \"" + samplerType + "\"");
    }
}

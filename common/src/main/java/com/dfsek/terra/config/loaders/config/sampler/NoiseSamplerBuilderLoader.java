package com.dfsek.terra.config.loaders.config.sampler;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.noise.normalizer.Normalizer;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.sampler.templates.DomainWarpTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.FastNoiseTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.ImageSamplerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.LinearNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.NormalNormalizerTemplate;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoiseSamplerBuilderLoader implements TypeLoader<NoiseSeeded> {

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
                Normalizer.NormalType normalType = loader.loadClass(Normalizer.NormalType.class, map.get("type"));
                switch(normalType) {
                    case LINEAR:
                        return loader.loadClass(LinearNormalizerTemplate.class, map).get();
                    case NORMAL:
                        return loader.loadClass(NormalNormalizerTemplate.class, map).get();
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

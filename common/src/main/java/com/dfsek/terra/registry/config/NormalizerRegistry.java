package com.dfsek.terra.registry.config;

import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.ClampNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.LinearNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.NormalNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.NormalizerTemplate;
import com.dfsek.terra.registry.TerraRegistry;

import java.util.function.Supplier;

public class NormalizerRegistry extends TerraRegistry<Supplier<NormalizerTemplate<?>>> {
    public NormalizerRegistry() {
        add("LINEAR", LinearNormalizerTemplate::new);
        add("NORMAL", NormalNormalizerTemplate::new);
        add("CLAMP", ClampNormalizerTemplate::new);
    }
}

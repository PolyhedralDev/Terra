package com.dfsek.terra.registry.config;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.math.noise.samplers.noise.WhiteNoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.simplex.OpenSimplex2SSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.api.math.noise.samplers.noise.simplex.PerlinSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.value.ValueCubicSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.value.ValueSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.sampler.templates.DomainWarpTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.ImageSamplerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.noise.CellularNoiseTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.noise.SimpleNoiseTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal.BrownianMotionTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal.PingPongTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal.RidgedFractalTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.ClampNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.LinearNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.NormalNormalizerTemplate;
import com.dfsek.terra.registry.TerraRegistry;

import java.util.function.Supplier;

public class NoiseRegistry extends TerraRegistry<Supplier<ObjectTemplate<NoiseSeeded>>> {
    public NoiseRegistry() {
        add("LINEAR", LinearNormalizerTemplate::new);
        add("NORMAL", NormalNormalizerTemplate::new);
        add("CLAMP", ClampNormalizerTemplate::new);

        add("IMAGE", ImageSamplerTemplate::new);

        add("DOMAIN_WARP", DomainWarpTemplate::new);

        add("FBM", BrownianMotionTemplate::new);
        add("PINGPONG", PingPongTemplate::new);
        add("RIDGED", RidgedFractalTemplate::new);

        add("OPENSIMPLEX2", () -> new SimpleNoiseTemplate(OpenSimplex2Sampler::new));
        add("OPENSIMPLEX2S", () -> new SimpleNoiseTemplate(OpenSimplex2SSampler::new));
        add("PERLIN", () -> new SimpleNoiseTemplate(PerlinSampler::new));


        add("VALUE", () -> new SimpleNoiseTemplate(ValueSampler::new));
        add("VALUECUBIC", () -> new SimpleNoiseTemplate(ValueCubicSampler::new));

        add("CELLULAR", CellularNoiseTemplate::new);

        add("WHITENOISE", () -> new SimpleNoiseTemplate(WhiteNoiseSampler::new));
    }
}

package com.dfsek.terra.addons.noise;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.noise.config.NoiseSamplerBuilderLoader;
import com.dfsek.terra.addons.noise.config.templates.DomainWarpTemplate;
import com.dfsek.terra.addons.noise.config.templates.ImageSamplerTemplate;
import com.dfsek.terra.addons.noise.config.templates.KernelTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.CellularNoiseTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.ConstantNoiseTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.ExpressionFunctionTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.GaborNoiseTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.SimpleNoiseTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.fractal.BrownianMotionTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.fractal.PingPongTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.fractal.RidgedFractalTemplate;
import com.dfsek.terra.addons.noise.config.templates.normalizer.ClampNormalizerTemplate;
import com.dfsek.terra.addons.noise.config.templates.normalizer.LinearNormalizerTemplate;
import com.dfsek.terra.addons.noise.config.templates.normalizer.NormalNormalizerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.random.GaussianNoiseSampler;
import com.dfsek.terra.addons.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2SSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.PerlinSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.SimplexSampler;
import com.dfsek.terra.addons.noise.samplers.noise.value.ValueCubicSampler;
import com.dfsek.terra.addons.noise.samplers.noise.value.ValueSampler;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Addon("config-noise-function")
@Author("Terra")
@Version("1.0.0")
public class NoiseAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin plugin;

    public static final TypeKey<Supplier<ObjectTemplate<SeededNoiseSampler>>> NOISE_SAMPLER_TOKEN = new TypeKey<>() {};

    @Override
    public void initialize() {
        plugin.getEventManager().registerListener(this, this);
    }

    public void packPreLoad(ConfigPackPreLoadEvent event) {
        CheckedRegistry<Supplier<ObjectTemplate<SeededNoiseSampler>>> noiseRegistry = event.getPack().getOrCreateRegistry(NOISE_SAMPLER_TOKEN);
        event.getPack()
                .applyLoader(SeededNoiseSampler.class, new NoiseSamplerBuilderLoader(noiseRegistry))
                .applyLoader(ImageSamplerTemplate.class, ImageSamplerTemplate::new)
                .applyLoader(DomainWarpTemplate.class, DomainWarpTemplate::new)
                .applyLoader(LinearNormalizerTemplate.class, LinearNormalizerTemplate::new)
                .applyLoader(NormalNormalizerTemplate.class, NormalNormalizerTemplate::new)
                .applyLoader(ClampNormalizerTemplate.class, ClampNormalizerTemplate::new);

        noiseRegistry.register("LINEAR", LinearNormalizerTemplate::new);
        noiseRegistry.register("NORMAL", NormalNormalizerTemplate::new);
        noiseRegistry.register("CLAMP", ClampNormalizerTemplate::new);

        noiseRegistry.register("IMAGE", ImageSamplerTemplate::new);

        noiseRegistry.register("DOMAINWARP", DomainWarpTemplate::new);

        noiseRegistry.register("FBM", BrownianMotionTemplate::new);
        noiseRegistry.register("PINGPONG", PingPongTemplate::new);
        noiseRegistry.register("RIDGED", RidgedFractalTemplate::new);

        noiseRegistry.register("OPENSIMPLEX2", () -> new SimpleNoiseTemplate(seed3 -> new OpenSimplex2Sampler()));
        noiseRegistry.register("OPENSIMPLEX2S", () -> new SimpleNoiseTemplate(seed3 -> new OpenSimplex2SSampler()));
        noiseRegistry.register("PERLIN", () -> new SimpleNoiseTemplate(seed2 -> new PerlinSampler()));
        noiseRegistry.register("SIMPLEX", () -> new SimpleNoiseTemplate(seed2 -> new SimplexSampler()));
        noiseRegistry.register("GABOR", GaborNoiseTemplate::new);


        noiseRegistry.register("VALUE", () -> new SimpleNoiseTemplate(ValueSampler::new));
        noiseRegistry.register("VALUECUBIC", () -> new SimpleNoiseTemplate(seed1 -> new ValueCubicSampler()));

        noiseRegistry.register("CELLULAR", CellularNoiseTemplate::new);

        noiseRegistry.register("WHITENOISE", () -> new SimpleNoiseTemplate(seed -> new WhiteNoiseSampler()));
        noiseRegistry.register("GAUSSIAN", () -> new SimpleNoiseTemplate(seed -> new GaussianNoiseSampler()));

        noiseRegistry.register("CONSTANT", ConstantNoiseTemplate::new);

        noiseRegistry.register("KERNEL", KernelTemplate::new);

        Map<String, SeededNoiseSampler> packFunctions = new HashMap<>();
        noiseRegistry.register("EXPRESSION", () -> new ExpressionFunctionTemplate(packFunctions));


        NoiseConfigPackTemplate template = new NoiseConfigPackTemplate();
        event.loadTemplate(template);
        packFunctions.putAll(template.getNoiseBuilderMap());
    }
}

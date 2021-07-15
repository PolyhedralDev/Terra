package com.dfsek.terra.addons.noise;

import com.dfsek.tectonic.exception.ConfigException;
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
import com.dfsek.terra.addons.noise.samplers.ImageSampler;
import com.dfsek.terra.addons.noise.samplers.noise.CellularSampler;
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
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.seeded.NoiseProvider;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

import java.util.HashMap;
import java.util.Map;

@Addon("config-noise-function")
@Author("Terra")
@Version("1.0.0")
public class NoiseAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin plugin;

    @Override
    public void initialize() {
        plugin.getEventManager().registerListener(this, this);
    }

    public void packPreLoad(ConfigPackPreLoadEvent event) {
        CheckedRegistry<NoiseProvider> noiseRegistry = event.getPack().getOrCreateRegistry(NoiseProvider.class);
        event.getPack()
                .applyLoader(NoiseSeeded.class, new NoiseSamplerBuilderLoader(noiseRegistry))
                .applyLoader(ImageSamplerTemplate.class, ImageSamplerTemplate::new)
                .applyLoader(DomainWarpTemplate.class, DomainWarpTemplate::new)
                .applyLoader(LinearNormalizerTemplate.class, LinearNormalizerTemplate::new)
                .applyLoader(NormalNormalizerTemplate.class, NormalNormalizerTemplate::new)
                .applyLoader(ImageSampler.Channel.class, (t, object, cf) -> ImageSampler.Channel.valueOf((String) object))
                .applyLoader(ClampNormalizerTemplate.class, ClampNormalizerTemplate::new)
                .applyLoader(CellularSampler.ReturnType.class, (t, object, cf) -> CellularSampler.ReturnType.valueOf((String) object))
                .applyLoader(CellularSampler.DistanceFunction.class, (t, object, cf) -> CellularSampler.DistanceFunction.valueOf((String) object));

        noiseRegistry.register("LINEAR", LinearNormalizerTemplate::new);
        noiseRegistry.register("NORMAL", NormalNormalizerTemplate::new);
        noiseRegistry.register("CLAMP", ClampNormalizerTemplate::new);

        noiseRegistry.register("IMAGE", ImageSamplerTemplate::new);

        noiseRegistry.register("DOMAINWARP", DomainWarpTemplate::new);

        noiseRegistry.register("FBM", BrownianMotionTemplate::new);
        noiseRegistry.register("PINGPONG", PingPongTemplate::new);
        noiseRegistry.register("RIDGED", RidgedFractalTemplate::new);

        noiseRegistry.register("OPENSIMPLEX2", () -> new SimpleNoiseTemplate(OpenSimplex2Sampler::new));
        noiseRegistry.register("OPENSIMPLEX2S", () -> new SimpleNoiseTemplate(OpenSimplex2SSampler::new));
        noiseRegistry.register("PERLIN", () -> new SimpleNoiseTemplate(PerlinSampler::new));
        noiseRegistry.register("SIMPLEX", () -> new SimpleNoiseTemplate(SimplexSampler::new));
        noiseRegistry.register("GABOR", GaborNoiseTemplate::new);


        noiseRegistry.register("VALUE", () -> new SimpleNoiseTemplate(ValueSampler::new));
        noiseRegistry.register("VALUECUBIC", () -> new SimpleNoiseTemplate(ValueCubicSampler::new));

        noiseRegistry.register("CELLULAR", CellularNoiseTemplate::new);

        noiseRegistry.register("WHITENOISE", () -> new SimpleNoiseTemplate(WhiteNoiseSampler::new));
        noiseRegistry.register("GAUSSIAN", () -> new SimpleNoiseTemplate(GaussianNoiseSampler::new));

        noiseRegistry.register("CONSTANT", ConstantNoiseTemplate::new);

        noiseRegistry.register("KERNEL", KernelTemplate::new);

        Map<String, NoiseSeeded> packFunctions = new HashMap<>();
        noiseRegistry.register("EXPRESSION", () -> new ExpressionFunctionTemplate(packFunctions));


        try {
            NoiseConfigPackTemplate template = new NoiseConfigPackTemplate();
            event.loadTemplate(template);
            packFunctions.putAll(template.getNoiseBuilderMap());
        } catch(ConfigException e) {
            throw new RuntimeException(e);
        }
    }
}

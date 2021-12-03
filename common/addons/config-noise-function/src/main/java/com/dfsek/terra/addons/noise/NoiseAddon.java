/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise;

import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.addons.noise.config.templates.DomainWarpTemplate;
import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;
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
import com.dfsek.terra.addons.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.addons.noise.samplers.noise.random.GaussianNoiseSampler;
import com.dfsek.terra.addons.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2SSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.PerlinSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.SimplexSampler;
import com.dfsek.terra.addons.noise.samplers.noise.value.ValueCubicSampler;
import com.dfsek.terra.addons.noise.samplers.noise.value.ValueSampler;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class NoiseAddon implements AddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<NoiseSampler>>> NOISE_SAMPLER_TOKEN = new TypeKey<>() {
    };
    @Inject
    private Platform plugin;
    
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        plugin.getEventManager()
              .getHandler(FunctionalEventHandler.class)
              .register(addon, ConfigPackPreLoadEvent.class)
              .then(event -> {
                  CheckedRegistry<Supplier<ObjectTemplate<NoiseSampler>>> noiseRegistry = event.getPack().getOrCreateRegistry(
                          NOISE_SAMPLER_TOKEN);
                  event.getPack()
                       .applyLoader(CellularSampler.DistanceFunction.class,
                                    (t, o, l) -> CellularSampler.DistanceFunction.valueOf((String) o))
                       .applyLoader(CellularSampler.ReturnType.class, (t, o, l) -> CellularSampler.ReturnType.valueOf((String) o))
                       .applyLoader(DimensionApplicableNoiseSampler.class, DimensionApplicableNoiseSampler::new)
                       .applyLoader(FunctionTemplate.class, FunctionTemplate::new);
            
                  noiseRegistry.register("LINEAR", LinearNormalizerTemplate::new);
                  noiseRegistry.register("NORMAL", NormalNormalizerTemplate::new);
                  noiseRegistry.register("CLAMP", ClampNormalizerTemplate::new);
            
                  noiseRegistry.register("IMAGE", ImageSamplerTemplate::new);
            
                  noiseRegistry.register("DOMAIN_WARP", DomainWarpTemplate::new);
            
                  noiseRegistry.register("FBM", BrownianMotionTemplate::new);
                  noiseRegistry.register("PING_PONG", PingPongTemplate::new);
                  noiseRegistry.register("RIDGED", RidgedFractalTemplate::new);
            
                  noiseRegistry.register("OPEN_SIMPLEX_2", () -> new SimpleNoiseTemplate(OpenSimplex2Sampler::new));
                  noiseRegistry.register("OPEN_SIMPLEX_2S", () -> new SimpleNoiseTemplate(OpenSimplex2SSampler::new));
                  noiseRegistry.register("PERLIN", () -> new SimpleNoiseTemplate(PerlinSampler::new));
                  noiseRegistry.register("SIMPLEX", () -> new SimpleNoiseTemplate(SimplexSampler::new));
                  noiseRegistry.register("GABOR", GaborNoiseTemplate::new);
            
            
                  noiseRegistry.register("VALUE", () -> new SimpleNoiseTemplate(ValueSampler::new));
                  noiseRegistry.register("VALUE_CUBIC", () -> new SimpleNoiseTemplate(ValueCubicSampler::new));
            
                  noiseRegistry.register("CELLULAR", CellularNoiseTemplate::new);
            
                  noiseRegistry.register("WHITE_NOISE", () -> new SimpleNoiseTemplate(WhiteNoiseSampler::new));
                  noiseRegistry.register("GAUSSIAN", () -> new SimpleNoiseTemplate(GaussianNoiseSampler::new));
            
                  noiseRegistry.register("CONSTANT", ConstantNoiseTemplate::new);
            
                  noiseRegistry.register("KERNEL", KernelTemplate::new);
            
                  Map<String, DimensionApplicableNoiseSampler> packSamplers = new LinkedHashMap<>();
                  Map<String, FunctionTemplate> packFunctions = new LinkedHashMap<>();
                  noiseRegistry.register("EXPRESSION", () -> new ExpressionFunctionTemplate(packSamplers, packFunctions));
            
            
                  NoiseConfigPackTemplate template = event.loadTemplate(new NoiseConfigPackTemplate());
                  packSamplers.putAll(template.getSamplers());
                  packFunctions.putAll(template.getFunctions());
              })
              .failThrough();
    }
}

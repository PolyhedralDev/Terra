/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.addons.noise.config.templates.BinaryArithmeticTemplate;
import com.dfsek.terra.addons.noise.config.templates.DomainWarpTemplate;
import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;
import com.dfsek.terra.addons.noise.config.templates.ImageSamplerTemplate;
import com.dfsek.terra.addons.noise.config.templates.KernelTemplate;
import com.dfsek.terra.addons.noise.config.templates.LinearHeightmapSamplerTemplate;
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
import com.dfsek.terra.addons.noise.config.templates.normalizer.PosterizationNormalizerTemplate;
import com.dfsek.terra.addons.noise.config.templates.normalizer.ProbabilityNormalizerTemplate;
import com.dfsek.terra.addons.noise.config.templates.normalizer.ScaleNormalizerTemplate;
import com.dfsek.terra.addons.noise.samplers.arithmetic.AdditionSampler;
import com.dfsek.terra.addons.noise.samplers.arithmetic.DivisionSampler;
import com.dfsek.terra.addons.noise.samplers.arithmetic.MaxSampler;
import com.dfsek.terra.addons.noise.samplers.arithmetic.MinSampler;
import com.dfsek.terra.addons.noise.samplers.arithmetic.MultiplicationSampler;
import com.dfsek.terra.addons.noise.samplers.arithmetic.SubtractionSampler;
import com.dfsek.terra.addons.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.addons.noise.samplers.noise.random.GaussianNoiseSampler;
import com.dfsek.terra.addons.noise.samplers.noise.random.PositiveWhiteNoiseSampler;
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
                                    (type, o, loader, depthTracker) -> CellularSampler.DistanceFunction.valueOf((String) o))
                       .applyLoader(CellularSampler.ReturnType.class,
                                    (type, o, loader, depthTracker) -> CellularSampler.ReturnType.valueOf((String) o))
                       .applyLoader(DimensionApplicableNoiseSampler.class, DimensionApplicableNoiseSampler::new)
                       .applyLoader(FunctionTemplate.class, FunctionTemplate::new);
            
                  noiseRegistry.register(addon.key("LINEAR"), LinearNormalizerTemplate::new);
                  noiseRegistry.register(addon.key("NORMAL"), NormalNormalizerTemplate::new);
                  noiseRegistry.register(addon.key("CLAMP"), ClampNormalizerTemplate::new);
                  noiseRegistry.register(addon.key("PROBABILITY"), ProbabilityNormalizerTemplate::new);
                  noiseRegistry.register(addon.key("SCALE"), ScaleNormalizerTemplate::new);
                  noiseRegistry.register(addon.key("POSTERIZATION"), PosterizationNormalizerTemplate::new);
            
                  noiseRegistry.register(addon.key("IMAGE"), ImageSamplerTemplate::new);
            
                  noiseRegistry.register(addon.key("DOMAIN_WARP"), DomainWarpTemplate::new);
            
                  noiseRegistry.register(addon.key("FBM"), BrownianMotionTemplate::new);
                  noiseRegistry.register(addon.key("PING_PONG"), PingPongTemplate::new);
                  noiseRegistry.register(addon.key("RIDGED"), RidgedFractalTemplate::new);
            
                  noiseRegistry.register(addon.key("OPEN_SIMPLEX_2"), () -> new SimpleNoiseTemplate(OpenSimplex2Sampler::new));
                  noiseRegistry.register(addon.key("OPEN_SIMPLEX_2S"), () -> new SimpleNoiseTemplate(OpenSimplex2SSampler::new));
                  noiseRegistry.register(addon.key("PERLIN"), () -> new SimpleNoiseTemplate(PerlinSampler::new));
                  noiseRegistry.register(addon.key("SIMPLEX"), () -> new SimpleNoiseTemplate(SimplexSampler::new));
                  noiseRegistry.register(addon.key("GABOR"), GaborNoiseTemplate::new);
            
            
                  noiseRegistry.register(addon.key("VALUE"), () -> new SimpleNoiseTemplate(ValueSampler::new));
                  noiseRegistry.register(addon.key("VALUE_CUBIC"), () -> new SimpleNoiseTemplate(ValueCubicSampler::new));
            
                  noiseRegistry.register(addon.key("CELLULAR"), CellularNoiseTemplate::new);
            
                  noiseRegistry.register(addon.key("WHITE_NOISE"), () -> new SimpleNoiseTemplate(WhiteNoiseSampler::new));
                  noiseRegistry.register(addon.key("POSITIVE_WHITE_NOISE"), () -> new SimpleNoiseTemplate(PositiveWhiteNoiseSampler::new));
                  noiseRegistry.register(addon.key("GAUSSIAN"), () -> new SimpleNoiseTemplate(GaussianNoiseSampler::new));
            
                  noiseRegistry.register(addon.key("CONSTANT"), ConstantNoiseTemplate::new);
            
                  noiseRegistry.register(addon.key("KERNEL"), KernelTemplate::new);
            
                  noiseRegistry.register(addon.key("LINEAR_HEIGHTMAP"), LinearHeightmapSamplerTemplate::new);
            
                  noiseRegistry.register(addon.key("ADD"), () -> new BinaryArithmeticTemplate<>(AdditionSampler::new));
                  noiseRegistry.register(addon.key("SUB"), () -> new BinaryArithmeticTemplate<>(SubtractionSampler::new));
                  noiseRegistry.register(addon.key("MUL"), () -> new BinaryArithmeticTemplate<>(MultiplicationSampler::new));
                  noiseRegistry.register(addon.key("DIV"), () -> new BinaryArithmeticTemplate<>(DivisionSampler::new));
                  noiseRegistry.register(addon.key("MAX"), () -> new BinaryArithmeticTemplate<>(MaxSampler::new));
                  noiseRegistry.register(addon.key("MIN"), () -> new BinaryArithmeticTemplate<>(MinSampler::new));
            
            
                  Map<String, DimensionApplicableNoiseSampler> packSamplers = new LinkedHashMap<>();
                  Map<String, FunctionTemplate> packFunctions = new LinkedHashMap<>();
                  noiseRegistry.register(addon.key("EXPRESSION"), () -> new ExpressionFunctionTemplate(packSamplers, packFunctions));
            
            
                  NoiseConfigPackTemplate template = event.loadTemplate(new NoiseConfigPackTemplate());
                  packSamplers.putAll(template.getSamplers());
                  packFunctions.putAll(template.getFunctions());
                  event.getPack().getContext().put(template);
              })
              .priority(50)
              .failThrough();
    }
}

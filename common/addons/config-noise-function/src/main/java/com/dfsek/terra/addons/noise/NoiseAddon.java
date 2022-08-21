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

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
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
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class NoiseAddon implements MonadAddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<NoiseSampler>>> NOISE_SAMPLER_TOKEN = new TypeKey<>() {
    };
    
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((functionalEventHandler, base, platform) -> Init.ofPure(
                        functionalEventHandler.register(base, ConfigPackPreLoadEvent.class)
                                              .then(event -> {
                                                  Registry<Supplier<ObjectTemplate<NoiseSampler>>> noiseRegistry = event.getPack().createRegistry(
                                                          NOISE_SAMPLER_TOKEN);
                                                  event.getPack()
                                                       .applyLoader(CellularSampler.DistanceFunction.class,
                                                                    (type, o, loader, depthTracker) -> CellularSampler.DistanceFunction.valueOf((String) o))
                                                       .applyLoader(CellularSampler.ReturnType.class,
                                                                    (type, o, loader, depthTracker) -> CellularSampler.ReturnType.valueOf((String) o))
                                                       .applyLoader(DimensionApplicableNoiseSampler.class, DimensionApplicableNoiseSampler::new)
                                                       .applyLoader(FunctionTemplate.class, FunctionTemplate::new);
    
                                                  noiseRegistry.register(base.key("LINEAR"), LinearNormalizerTemplate::new);
                                                  noiseRegistry.register(base.key("NORMAL"), NormalNormalizerTemplate::new);
                                                  noiseRegistry.register(base.key("CLAMP"), ClampNormalizerTemplate::new);
                                                  noiseRegistry.register(base.key("PROBABILITY"), ProbabilityNormalizerTemplate::new);
                                                  noiseRegistry.register(base.key("SCALE"), ScaleNormalizerTemplate::new);
                                                  noiseRegistry.register(base.key("POSTERIZATION"), PosterizationNormalizerTemplate::new);
    
                                                  noiseRegistry.register(base.key("IMAGE"), ImageSamplerTemplate::new);
    
                                                  noiseRegistry.register(base.key("DOMAIN_WARP"), DomainWarpTemplate::new);
    
                                                  noiseRegistry.register(base.key("FBM"), BrownianMotionTemplate::new);
                                                  noiseRegistry.register(base.key("PING_PONG"), PingPongTemplate::new);
                                                  noiseRegistry.register(base.key("RIDGED"), RidgedFractalTemplate::new);
    
                                                  noiseRegistry.register(base.key("OPEN_SIMPLEX_2"), () -> new SimpleNoiseTemplate(OpenSimplex2Sampler::new));
                                                  noiseRegistry.register(base.key("OPEN_SIMPLEX_2S"), () -> new SimpleNoiseTemplate(OpenSimplex2SSampler::new));
                                                  noiseRegistry.register(base.key("PERLIN"), () -> new SimpleNoiseTemplate(PerlinSampler::new));
                                                  noiseRegistry.register(base.key("SIMPLEX"), () -> new SimpleNoiseTemplate(SimplexSampler::new));
                                                  noiseRegistry.register(base.key("GABOR"), GaborNoiseTemplate::new);
    
    
                                                  noiseRegistry.register(base.key("VALUE"), () -> new SimpleNoiseTemplate(ValueSampler::new));
                                                  noiseRegistry.register(base.key("VALUE_CUBIC"), () -> new SimpleNoiseTemplate(ValueCubicSampler::new));
    
                                                  noiseRegistry.register(base.key("CELLULAR"), CellularNoiseTemplate::new);
    
                                                  noiseRegistry.register(base.key("WHITE_NOISE"), () -> new SimpleNoiseTemplate(WhiteNoiseSampler::new));
                                                  noiseRegistry.register(base.key("POSITIVE_WHITE_NOISE"), () -> new SimpleNoiseTemplate(PositiveWhiteNoiseSampler::new));
                                                  noiseRegistry.register(base.key("GAUSSIAN"), () -> new SimpleNoiseTemplate(GaussianNoiseSampler::new));
    
                                                  noiseRegistry.register(base.key("CONSTANT"), ConstantNoiseTemplate::new);
    
                                                  noiseRegistry.register(base.key("KERNEL"), KernelTemplate::new);
    
                                                  noiseRegistry.register(base.key("LINEAR_HEIGHTMAP"), LinearHeightmapSamplerTemplate::new);
    
                                                  noiseRegistry.register(base.key("ADD"), () -> new BinaryArithmeticTemplate<>(AdditionSampler::new));
                                                  noiseRegistry.register(base.key("SUB"), () -> new BinaryArithmeticTemplate<>(SubtractionSampler::new));
                                                  noiseRegistry.register(base.key("MUL"), () -> new BinaryArithmeticTemplate<>(MultiplicationSampler::new));
                                                  noiseRegistry.register(base.key("DIV"), () -> new BinaryArithmeticTemplate<>(DivisionSampler::new));
                                                  noiseRegistry.register(base.key("MAX"), () -> new BinaryArithmeticTemplate<>(MaxSampler::new));
                                                  noiseRegistry.register(base.key("MIN"), () -> new BinaryArithmeticTemplate<>(MinSampler::new));
    
    
                                                  Map<String, DimensionApplicableNoiseSampler> packSamplers = new LinkedHashMap<>();
                                                  Map<String, FunctionTemplate> packFunctions = new LinkedHashMap<>();
                                                  noiseRegistry.register(base.key("EXPRESSION"), () -> new ExpressionFunctionTemplate(packSamplers, packFunctions));
    
    
                                                  NoiseConfigPackTemplate template = event.loadTemplate(new NoiseConfigPackTemplate());
                                                  packSamplers.putAll(template.getSamplers());
                                                  packFunctions.putAll(template.getFunctions());
                                                  event.getPack().getContext().put(template);
                                              })
                                              .priority(50)
                                              .failThrough()))
                      );
    
    }
}

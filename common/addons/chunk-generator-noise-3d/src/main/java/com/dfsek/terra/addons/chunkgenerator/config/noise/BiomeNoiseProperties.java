package com.dfsek.terra.addons.chunkgenerator.config.noise;

import com.dfsek.seismic.type.sampler.Sampler;

import com.dfsek.terra.api.properties.Properties;


public record BiomeNoiseProperties(Sampler base,
                                   Sampler elevation,
                                   Sampler carving,
                                   int blendDistance,
                                   int blendStep,
                                   double blendWeight,
                                   double elevationWeight,
                                   ThreadLocalNoiseHolder noiseHolder) implements Properties {
}

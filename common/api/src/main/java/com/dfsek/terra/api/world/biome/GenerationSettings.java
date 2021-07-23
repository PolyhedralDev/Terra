package com.dfsek.terra.api.world.biome;

import com.dfsek.terra.api.noise.NoiseSampler;

public interface GenerationSettings {
    /**
     * Gets the noise sampler instance to use for base terrain.
     *
     * @return NoiseSampler for terrain
     */
    NoiseSampler getBaseSampler();

    /**
     * Gets the noise sampler to use for elevation
     *
     * @return NoiseSampler for elevation.
     */
    NoiseSampler getElevationSampler();

    /**
     * Gets the noise sampler to use for carving.
     *
     * @return NoiseSampler for carving.
     */
    NoiseSampler getCarver();

    int getBlendDistance();

    double getWeight();

    NoiseSampler getBiomeNoise();

    double getElevationWeight();

    int getBlendStep();
}

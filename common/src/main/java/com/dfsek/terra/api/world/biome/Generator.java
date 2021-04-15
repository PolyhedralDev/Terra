package com.dfsek.terra.api.world.biome;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.world.generation.math.samplers.terrain.TerrainSampler;

import java.util.List;

public interface Generator {
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

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    Palette getPalette(int y);

    NoiseSampler getBiomeNoise();

    double getElevationWeight();

    int getBlendStep();

    List<TerrainSampler> getTerrainSamplers();
}

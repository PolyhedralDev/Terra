package com.dfsek.terra.api.util.seeded;

import com.dfsek.terra.api.noise.NoiseSampler;

public interface NoiseSeeded extends SeededBuilder<NoiseSampler> {
    @Override
    NoiseSampler apply(Long seed);

    int getDimensions();
}

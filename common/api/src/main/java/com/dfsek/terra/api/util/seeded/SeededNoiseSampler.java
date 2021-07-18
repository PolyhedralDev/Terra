package com.dfsek.terra.api.util.seeded;

import com.dfsek.terra.api.noise.NoiseSampler;

public interface SeededNoiseSampler extends SeededBuilder<NoiseSampler> {
    static SeededNoiseSampler zero(int dimensions) {
        return new SeededNoiseSampler() {
            @Override
            public NoiseSampler apply(Long seed) {
                return NoiseSampler.zero();
            }

            @Override
            public int getDimensions() {
                return dimensions;
            }
        };
    }

    @Override
    NoiseSampler apply(Long seed);

    int getDimensions();
}

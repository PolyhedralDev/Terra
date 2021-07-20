package com.dfsek.terra.api.util.seeded;

import com.dfsek.terra.api.noise.NoiseSampler;

public interface SeededNoiseSampler extends SeededBuilder<NoiseSampler> {

    int getDimensions();
}

package com.dfsek.terra.addons.noise;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

import java.util.Map;

public class NoiseConfigPackTemplate implements ConfigTemplate {
    @Value("noise")
    private Map<String, DimensionApplicableNoiseSampler> noiseBuilderMap;

    public Map<String, DimensionApplicableNoiseSampler> getNoiseBuilderMap() {
        return noiseBuilderMap;
    }
}

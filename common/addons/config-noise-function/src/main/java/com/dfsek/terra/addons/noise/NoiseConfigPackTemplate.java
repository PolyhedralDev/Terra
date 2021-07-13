package com.dfsek.terra.addons.noise;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

import java.util.Map;

public class NoiseConfigPackTemplate implements ConfigTemplate {
    @Value("noise")
    private Map<String, NoiseSeeded> noiseBuilderMap;

    public Map<String, NoiseSeeded> getNoiseBuilderMap() {
        return noiseBuilderMap;
    }
}

package com.dfsek.terra.addons.biome.pipeline.config.stage;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.api.noise.NoiseSampler;

public abstract class StageTemplate implements ObjectTemplate<Stage> {
    @Value("noise")
    protected NoiseSampler noise;
}

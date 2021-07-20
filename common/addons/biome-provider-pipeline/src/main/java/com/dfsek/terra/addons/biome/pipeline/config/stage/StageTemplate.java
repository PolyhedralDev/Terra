package com.dfsek.terra.addons.biome.pipeline.config.stage;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.biome.pipeline.StageSeeded;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;
import com.dfsek.terra.api.util.seeded.SeededBuilder;

public abstract class StageTemplate implements ObjectTemplate<SeededBuilder<Stage>>, StageSeeded {
    @Value("noise")
    protected NoiseSampler noise;

    @Override
    public StageSeeded get() {
        return this;
    }
}

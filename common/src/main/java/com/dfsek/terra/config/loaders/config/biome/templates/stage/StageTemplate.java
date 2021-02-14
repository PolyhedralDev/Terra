package com.dfsek.terra.config.loaders.config.biome.templates.stage;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.util.seeded.StageSeeded;
import com.dfsek.terra.biome.pipeline.stages.Stage;

public abstract class StageTemplate implements ObjectTemplate<SeededBuilder<Stage>>, StageSeeded {
    @Value("noise")
    protected NoiseSeeded noise;

    @Override
    public StageSeeded get() {
        return this;
    }
}

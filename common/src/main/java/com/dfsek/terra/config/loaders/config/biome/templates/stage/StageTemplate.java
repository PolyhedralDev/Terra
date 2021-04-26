package com.dfsek.terra.config.loaders.config.biome.templates.stage;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.util.seeded.StageSeeded;
import com.dfsek.terra.api.world.biome.pipeline.stages.Stage;

@AutoDocAlias("StageSeeded")
public abstract class StageTemplate implements ObjectTemplate<SeededBuilder<Stage>>, StageSeeded {
    /**
     * Noise function to use for mutating biomes in this stage.
     */
    @Value("noise")
    protected NoiseSeeded noise;

    @Override
    public StageSeeded get() {
        return this;
    }
}

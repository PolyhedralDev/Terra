package com.dfsek.terra.config.loaders.config.biome.templates.stage.expander;

import com.dfsek.terra.api.world.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.api.world.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.api.world.biome.generation.pipeline.Stage;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.StageTemplate;

public class ExpanderStageTemplate extends StageTemplate {
    @Override
    public Stage apply(Long seed) {
        return new ExpanderStage(new FractalExpander(noise.apply(seed)));
    }
}

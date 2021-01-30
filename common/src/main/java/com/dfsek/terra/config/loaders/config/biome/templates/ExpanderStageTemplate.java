package com.dfsek.terra.config.loaders.config.biome.templates;

import com.dfsek.terra.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.biome.pipeline.stages.Stage;

public class ExpanderStageTemplate extends StageTemplate {
    @Override
    public Stage apply(Long seed) {
        return new ExpanderStage(new FractalExpander(noise.apply(seed)));
    }
}

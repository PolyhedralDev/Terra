package com.dfsek.terra.addons.biome.pipeline.config.stage.expander;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.addons.biome.pipeline.stages.ExpanderStage;

public class ExpanderStageTemplate extends StageTemplate {
    @Override
    public Stage build(long seed) {
        return new ExpanderStage(new FractalExpander(noise));
    }
}

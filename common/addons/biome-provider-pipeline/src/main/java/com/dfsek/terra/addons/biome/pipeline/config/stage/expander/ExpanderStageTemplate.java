/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config.stage.expander;

import com.dfsek.terra.addons.biome.pipeline.api.stage.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.addons.biome.pipeline.stages.ExpanderStage;


public class ExpanderStageTemplate extends StageTemplate {
    @Override
    public Stage get() {
        return new ExpanderStage(new FractalExpander(noise));
    }
}

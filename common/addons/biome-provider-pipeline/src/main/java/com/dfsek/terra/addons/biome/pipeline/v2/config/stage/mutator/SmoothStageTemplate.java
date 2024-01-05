/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.v2.config.stage.mutator;

import com.dfsek.terra.addons.biome.pipeline.v2.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.v2.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.v2.stage.mutators.SmoothStage;


public class SmoothStageTemplate extends StageTemplate {
    @Override
    public Stage get() {
        return new SmoothStage(noise);
    }
}

/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.stage.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.mutator.SmoothMutator;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;


public class SmoothMutatorTemplate extends StageTemplate {
    @Override
    public Stage get() {
        return new MutatorStage(new SmoothMutator(noise));
    }
}

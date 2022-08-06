/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config.stage.expander;

import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.Expander;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.stage.expander.FractalExpander;


public class ExpanderStageTemplate extends StageTemplate {
    @Override
    public Expander get() {
        return new FractalExpander(noise);
    }
}

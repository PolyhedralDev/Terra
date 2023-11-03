/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.v2.config.stage.expander;

import com.dfsek.terra.addons.biome.pipeline.v2.api.Expander;
import com.dfsek.terra.addons.biome.pipeline.v2.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.v2.stage.expander.FractalExpander;


public class ExpanderStageTemplate extends StageTemplate {
    @Override
    public Expander get() {
        return new FractalExpander(noise);
    }
}

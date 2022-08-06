/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Map;

import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.stage.mutators.ReplaceListMutator;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@SuppressWarnings("unused")
public class ReplaceListMutatorTemplate extends StageTemplate {
    @Value("default-from")
    private @Meta String defaultFrom;
    
    @Value("default-to")
    private @Meta ProbabilityCollection<@Meta PipelineBiome> defaultTo;
    
    @Value("to")
    private @Meta Map<@Meta PipelineBiome, @Meta ProbabilityCollection<@Meta PipelineBiome>> replace;
    
    @Override
    public Stage get() {
        return new ReplaceListMutator(replace, defaultFrom, defaultTo, noise);
    }
}

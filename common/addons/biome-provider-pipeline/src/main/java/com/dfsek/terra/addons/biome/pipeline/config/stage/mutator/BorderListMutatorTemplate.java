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
import com.dfsek.terra.addons.biome.pipeline.reimplementation.stage.mutators.BorderListMutator;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@SuppressWarnings("unused")
public class BorderListMutatorTemplate extends StageTemplate {
    @Value("from")
    private @Meta String from;
    
    @Value("default-replace")
    private @Meta String defaultReplace;
    
    @Value("default-to")
    private @Meta ProbabilityCollection<@Meta PipelineBiome> defaultTo;
    
    @Value("replace")
    private @Meta Map<@Meta PipelineBiome, @Meta ProbabilityCollection<@Meta PipelineBiome>> replace;
    
    
    @Override
    public Stage get() {
        return new BorderListMutator(replace, from, defaultReplace, noise, defaultTo);
    }
}

/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.Biome;


@SuppressWarnings("unused")
public class ReplaceMutatorTemplate extends StageTemplate {
    @Value("from")
    private @Meta String from;
    
    @Value("to")
    private @Meta ProbabilityCollection<@Meta Biome> to;
    
    @Override
    public Stage get() {
        return new MutatorStage(new ReplaceMutator(from, to, noise));
    }
}

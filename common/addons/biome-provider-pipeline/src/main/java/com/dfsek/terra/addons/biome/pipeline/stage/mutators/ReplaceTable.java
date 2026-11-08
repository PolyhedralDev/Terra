/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.stage.mutators;

import java.util.Map;

import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


final class ReplaceTable {
    private ReplaceTable() { }

    @SuppressWarnings("unchecked")
    static ProbabilityCollection<PipelineBiome>[] byId(Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace) {
        int max = -1;
        for(PipelineBiome key : replace.keySet()) max = Math.max(max, key.getIntID());
        ProbabilityCollection<PipelineBiome>[] table = new ProbabilityCollection[max + 1];
        replace.forEach((key, value) -> table[key.getIntID()] = value);
        return table;
    }
}

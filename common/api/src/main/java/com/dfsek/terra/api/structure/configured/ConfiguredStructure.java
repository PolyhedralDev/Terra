/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.configured;

import org.jetbrains.annotations.ApiStatus.Experimental;

import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@Experimental
public interface ConfiguredStructure extends StringIdentifiable {
    ProbabilityCollection<Structure> getStructure();
    
    Range getSpawnStart();
    
    StructureSpawn getSpawn();
}

/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure;

import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class StructureTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;
    
    @Value("scripts")
    private @Meta ProbabilityCollection<@Meta Structure> structure;
    
    @Value("spawn.start")
    private @Meta Range y;
    
    @Value("spawn")
    private @Meta StructureSpawn spawn;
    
    public String getID() {
        return id;
    }
    
    public ProbabilityCollection<Structure> getStructures() {
        return structure;
    }
    
    public Range getY() {
        return y;
    }
    
    public StructureSpawn getSpawn() {
        return spawn;
    }
}

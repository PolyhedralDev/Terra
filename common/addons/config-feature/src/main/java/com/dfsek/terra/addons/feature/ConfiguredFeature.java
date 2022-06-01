/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.structure.feature.Feature;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.WritableWorld;


public class ConfiguredFeature implements Feature {
    private final ProbabilityCollection<Structure> structures;
    
    private final NoiseSampler structureSelector;
    private final Distributor distributor;
    private final Locator locator;
    
    private final String id;
    
    public ConfiguredFeature(ProbabilityCollection<Structure> structures, NoiseSampler structureSelector, Distributor distributor,
                             Locator locator, String id) {
        this.structures = structures;
        this.structureSelector = structureSelector;
        this.distributor = distributor;
        this.locator = locator;
        this.id = id;
    }
    
    @Override
    public Structure getStructure(WritableWorld world, int x, int y, int z) {
        return structures.get(structureSelector, x, y, z, world.getSeed());
    }
    
    @Override
    public Distributor getDistributor() {
        return distributor;
    }
    
    @Override
    public Locator getLocator() {
        return locator;
    }
    
    @Override
    public String getID() {
        return id;
    }
}

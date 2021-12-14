/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.stages;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.Stage;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeMutator;


public class MutatorStage implements Stage {
    private final BiomeMutator mutator;
    
    public MutatorStage(BiomeMutator mutator) {
        this.mutator = mutator;
    }
    
    @Override
    public BiomeHolder apply(BiomeHolder in, long seed) {
        in.mutate(mutator, seed);
        return in;
    }
    
    @Override
    public boolean isExpansion() {
        return false;
    }
    
    @Override
    public Iterable<BiomeDelegate> getBiomes(Iterable<BiomeDelegate> biomes) {
        return mutator.getBiomes(biomes);
    }
    
    public enum Type {
        REPLACE,
        REPLACE_LIST,
        BORDER,
        BORDER_LIST,
        SMOOTH
    }
}

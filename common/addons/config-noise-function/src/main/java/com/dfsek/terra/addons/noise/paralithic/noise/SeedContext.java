/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.paralithic.noise;

import com.dfsek.paralithic.functions.dynamic.Context;


public class SeedContext implements Context {
    private final long seed;
    
    public SeedContext(long seed) {
        this.seed = seed;
    }
    
    public long getSeed() {
        return seed;
    }
}

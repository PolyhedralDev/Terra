/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.paralithic.noise;

import com.dfsek.paralithic.functions.dynamic.Context;
import com.dfsek.paralithic.functions.dynamic.DynamicFunction;
import com.dfsek.paralithic.node.Statefulness;

import com.dfsek.terra.api.noise.NoiseSampler;


public class NoiseFunction2 implements DynamicFunction {
    private final NoiseSampler gen;
    
    public NoiseFunction2(NoiseSampler gen) {
        this.gen = gen;
    }
    
    @Override
    public double eval(double... args) {
        throw new UnsupportedOperationException("Cannot evaluate seeded function without seed context.");
    }
    
    @Override
    public double eval(Context context, double... args) {
        return gen.noise(((SeedContext) context).getSeed(), args[0], args[1]);
    }
    
    @Override
    public int getArgNumber() {
        return 2;
    }
    
    @Override
    public Statefulness statefulness() {
        return Statefulness.CONTEXTUAL;
    }
}

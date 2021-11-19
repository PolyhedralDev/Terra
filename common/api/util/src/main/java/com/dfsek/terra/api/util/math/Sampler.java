/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.math;

@FunctionalInterface
public interface Sampler {
    double sample(double x, double y, double z);
    
    default double sample(int x, int y,
                          int z) { // Floating-point modulus operations are expensive. This allows implementations to optionally handle
        // integers separately.
        return sample((double) x, y, z);
    }
}

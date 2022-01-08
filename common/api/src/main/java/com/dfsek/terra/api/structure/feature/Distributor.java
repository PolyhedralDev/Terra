/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.feature;

public interface Distributor {
    static Distributor yes() {
        return (x, z, seed) -> true;
    }
    
    static Distributor no() {
        return (x, z, seed) -> false;
    }
    
    boolean matches(int x, int z, long seed);
    
    default Distributor and(Distributor other) {
        return (x, z, seed) -> this.matches(x, z, seed) && other.matches(x, z, seed);
    }
    
    default Distributor or(Distributor other) {
        return (x, z, seed) -> this.matches(x, z, seed) || other.matches(x, z, seed);
    }
    
    default Distributor xor(Distributor other) {
        return (x, z, seed) -> this.matches(x, z, seed) ^ other.matches(x, z, seed);
    }
}

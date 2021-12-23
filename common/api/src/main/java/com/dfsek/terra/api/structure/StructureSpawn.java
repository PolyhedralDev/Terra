/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure;

import org.jetbrains.annotations.ApiStatus.Experimental;

import com.dfsek.terra.api.util.vector.Vector3;


@Experimental
public interface StructureSpawn {
    /**
     * Get nearest spawn point
     *
     * @param x    X coordinate
     * @param z    Z coordinate
     * @param seed Seed for RNG
     *
     * @return Vector representing nearest spawnpoint
     */
    Vector3 getNearestSpawn(int x, int z, long seed);
}

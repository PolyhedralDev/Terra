/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure;

import com.dfsek.seismic.type.vector.Vector3;
import org.jetbrains.annotations.ApiStatus.Experimental;


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

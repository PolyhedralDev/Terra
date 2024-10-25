/*
 * Copyright (c) 2020-2024 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor.distributors;

import com.dfsek.terra.addons.feature.distributor.util.Point;
import com.dfsek.terra.api.structure.feature.Distributor;

import java.util.Set;


public class PointSetDistributor implements Distributor {
    private final Set<Point> points;

    public PointSetDistributor(Set<Point> points) {
        this.points = points;
    }

    @Override
    public boolean matches(int x, int z, long seed) {
        return points.contains(new Point(x, z));
    }
}

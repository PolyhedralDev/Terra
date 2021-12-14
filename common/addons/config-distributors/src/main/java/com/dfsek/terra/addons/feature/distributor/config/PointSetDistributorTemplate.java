/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.Set;

import com.dfsek.terra.addons.feature.distributor.distributors.PointSetDistributor;
import com.dfsek.terra.addons.feature.distributor.util.Point;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Distributor;


public class PointSetDistributorTemplate implements ObjectTemplate<Distributor> {
    @Value("points")
    private @Meta Set<@Meta Point> points;
    
    @Override
    public Distributor get() {
        return new PointSetDistributor(points);
    }
}

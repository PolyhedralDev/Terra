/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor.util;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.config.meta.Meta;


public class PointTemplate implements ObjectTemplate<Point> {
    @Value("x")
    private @Meta int x;
    
    @Value("z")
    private @Meta int z;
    
    @Override
    public Point get() {
        return new Point(x, z);
    }
}

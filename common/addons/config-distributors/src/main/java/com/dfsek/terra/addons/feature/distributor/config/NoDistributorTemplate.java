/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.structure.feature.Distributor;


public class NoDistributorTemplate implements ObjectTemplate<Distributor> {
    @Override
    public Distributor get() {
        return Distributor.no();
    }
}
